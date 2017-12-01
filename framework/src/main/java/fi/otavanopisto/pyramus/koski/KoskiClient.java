package fi.otavanopisto.pyramus.koski;

import java.io.StringWriter;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.koski.model.Henkilo;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.apa.KoskiAPAStudentHandler;
import fi.otavanopisto.pyramus.koski.model.result.OpiskeluoikeusReturnVal;
import fi.otavanopisto.pyramus.koski.model.result.OppijaReturnVal;

/**
 * https://dev.koski.opintopolku.fi/koski/documentation
 */
@RequestScoped
public class KoskiClient {

  private static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
  private static final String KOSKI_HENKILO_OID = "koski.henkilo-oid";
  private static final String KOSKI_SKIPPED_STUDENT = "koski.skippedStudent";
  
  private static final String KOSKI_SETTINGKEY_BASEURL = "koski.baseUrl";
  private static final String KOSKI_SETTINGKEY_AUTH = "koski.auth";
  
  @Inject
  private Logger logger;
  
  @Inject
  private KoskiSettings settings;
  
  @Inject
  private SettingKeyDAO settingKeyDAO;

  @Inject
  private SettingDAO settingDAO;

  @Inject 
  private PersonVariableDAO personVariableDAO;
  
  @Inject
  private StudentDAO studentDAO;
  
  @Inject 
  private UserVariableDAO userVariableDAO;

  @Inject
  private KoskiPersonLogDAO koskiPersonLogDAO;
  
  @Inject
  private KoskiAPAStudentHandler apaHandler;

  @Inject
  private KoskiLukioStudentHandler lukioHandler;

  @Inject
  private KoskiAikuistenPerusopetuksenStudentHandler aikuistenPerusopetuksenHandler;
  
  private String getBaseUrl() {
    return getSetting(KOSKI_SETTINGKEY_BASEURL);
  }
  
  private String getAuth() {
    return getSetting(KOSKI_SETTINGKEY_AUTH);
  }
  
  private String getSetting(String settingName) {
    SettingKey key = settingKeyDAO.findByName(settingName);
    if (key != null) {
      Setting setting = settingDAO.findByKey(key);
      
      if (setting != null)
        return setting.getValue();
    }
    
    return null;
  }
  
  public void updateStudent(Student student) throws KoskiException {
    try {
      if (!settings.isEnabled())
        return;
      
      if (student == null) {
        logger.log(Level.WARNING, "updateStudent called with null student.");
        return;
      }
      
      if (student.getPerson() == null || student.getStudyProgramme() == null) {
        logger.log(Level.WARNING, String.format("Can not update student (%d) with missing person or studyprogramme.", student.getId()));
        return;
      }

      // Does the person have any reported study programmes
      if (!student.getPerson().getStudents().stream().anyMatch((Student s) -> isReportedStudent(s))) {
        return;
      }

      clearPersonLog(student.getPerson());
      
      String personOid = personVariableDAO.findByPersonAndKey(student.getPerson(), KOSKI_HENKILO_OID);
      
      if (StringUtils.isBlank(student.getPerson().getSocialSecurityNumber()) && StringUtils.isBlank(personOid)) {
        logger.warning(String.format("Can not update person (%d) without SSN or OID.", student.getPerson().getId()));
        return;
      }
      
      Henkilo henkilo;
      if (StringUtils.isNotBlank(personOid))
        henkilo = new HenkiloTiedotJaOID(personOid, student.getPerson().getSocialSecurityNumber(), student.getFirstName(), student.getLastName(), getCallname(student));
      else
        henkilo = new HenkiloUusi(student.getPerson().getSocialSecurityNumber(), student.getFirstName(), student.getLastName(), getCallname(student));

      Oppija oppija = new Oppija();
      oppija.setHenkilo(henkilo);
      
      List<Student> reportedStudents = new ArrayList<>();
      
      for (Student s : student.getPerson().getStudents()) {
        if (isReportedStudent(student)) {
          Opiskeluoikeus o = studentToOpiskeluoikeus(s);
          if (o != null) {
            oppija.addOpiskeluoikeus(o);
            reportedStudents.add(s);
          }
        }
      }
      
      if (oppija.getOpiskeluoikeudet().size() == 0) {
        logger.info(String.format("Updating person %d was skipped due to no updateable study permits.", student.getPerson().getId()));
        return;
      }
      
      String uri = String.format("%s/oppija", getBaseUrl());
      
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target(uri);
      Builder request = target
          .request(MediaType.APPLICATION_JSON_TYPE)
          .header("Authorization", "Basic " + getAuth());

      
      ObjectMapper mapper = new ObjectMapper();
      StringWriter writer = new StringWriter();
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      mapper.writeValue(writer, oppija);
      
      String requestStr = writer.toString();

      Response response = request.put(Entity.json(requestStr));
      if (response.getStatus() == 200) {
        String ret = response.readEntity(String.class);
        
        // For test environment the oid's are not saved
        if (!settings.isTestEnvironment()) {
          OppijaReturnVal oppijaReturnVal = mapper.readValue(ret, OppijaReturnVal.class);
          String servedPersonOid = oppijaReturnVal.getHenkilo().getOid();
          
          if (StringUtils.isEmpty(personOid)) {
            // If the oid was empty in db, save the given one
            personVariableDAO.setPersonVariable(student.getPerson(), KOSKI_HENKILO_OID, servedPersonOid);
          } else {
            // Validate the oid is the same
            if (!StringUtils.equals(personOid, servedPersonOid))
              throw new RuntimeException("Returned person oid doesn't match the saved oid");
          }

          for (OpiskeluoikeusReturnVal opiskeluoikeus : oppijaReturnVal.getOpiskeluoikeudet()) {
            if (opiskeluoikeus.getLahdejarjestelmanId() != null && 
                opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma() != null &&
                StringUtils.equals(opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma().getKoodiarvo(), "pyramus")) {
              long servedStudentId = NumberUtils.toLong(opiskeluoikeus.getLahdejarjestelmanId().getId(), -1);
              if (servedStudentId != -1) {
                Student reportedStudent = studentDAO.findById(servedStudentId);
                String studyOid = userVariableDAO.findByUserAndKey(reportedStudent, KOSKI_STUDYPERMISSION_ID);
                String servedStudyOid = opiskeluoikeus.getOid();

                if (StringUtils.isBlank(studyOid)) {
                  userVariableDAO.setUserVariable(reportedStudent, KOSKI_STUDYPERMISSION_ID, servedStudyOid);
                } else {
                  // Validate the oid is the same
                  if (!StringUtils.equals(studyOid, servedStudyOid))
                    throw new RuntimeException(String.format("Returned study permit oid %s doesn't match the saved oid %s.", servedStudyOid, studyOid));
                }
              } else {
                logger.log(Level.WARNING, String.format("Could not update student oid because returned source system id was -1 (Person %d).", student.getPerson().getId()));
              }
            } else {
              logger.log(Level.WARNING, String.format("Could not update student oid because returned source system was not defined or isn't this system."));
            }
          }
        }

        // Log successful event
        koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.SUCCESS, new Date());
        logger.info(String.format("KoskiClient: successfully updated person %d.", student.getPerson().getId()));
      } else {
        String ret = response.readEntity(String.class);
        // Log failed event
        koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.SERVER_FAILURE, new Date());
        logger.log(Level.SEVERE, String.format("Koski server returned %d when trying to create person %d. Content %s", response.getStatus(), student.getPerson().getId(), ret));
      }
    } catch (Exception ex) {
      try {
        KoskiPersonState reason = KoskiPersonState.UNKNOWN_FAILURE;
        if (ex instanceof KoskiException) {
          reason = ((KoskiException) ex).getState();
        }
        
        if (ex instanceof ProcessingException) {
          ProcessingException pe = (ProcessingException) ex;
          
          if (pe.getCause() instanceof ConnectException) {
            reason = KoskiPersonState.SERVER_UNAVAILABLE;
          }
        }
        
        // Log failed event
        koskiPersonLogDAO.create(student.getPerson(), reason, new Date());
      } catch (Exception e) {
      }
      
      logger.log(Level.SEVERE, String.format("Unknown failure while updating person %d", student.getPerson().getId()), ex);
    }
  }

  private void clearPersonLog(Person person) {
    List<KoskiPersonLog> entries = koskiPersonLogDAO.listByPerson(person);
    entries.forEach(entry -> koskiPersonLogDAO.delete(entry));
  }

  private boolean isReportedStudent(Student student) {
    return 
        settings.isEnabledStudyProgramme(student.getStudyProgramme()) &&
        !Boolean.valueOf(userVariableDAO.findByUserAndKey(student, KOSKI_SKIPPED_STUDENT));
  }
  
  private String getCallname(Student student) {
    if (StringUtils.isNotBlank(student.getNickname()) && (StringUtils.containsIgnoreCase(student.getFirstName(), student.getNickname())))
      return student.getNickname();
    else {
      if (StringUtils.isNotBlank(student.getFirstName())) {
        String[] split = StringUtils.split(StringUtils.trim(student.getFirstName()), ' ');
        if (split != null && split.length > 0)
          return split[0];
      }
    }
    
    return null;
  }

  private Opiskeluoikeus studentToOpiskeluoikeus(Student student) throws KoskiException {
    KoskiStudyProgrammeHandler handler = settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId());
    switch (handler) {
      case aikuistenperusopetus:
        return aikuistenPerusopetuksenHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.aikuistenperusopetus);
      case aineopiskeluperusopetus:
        return aikuistenPerusopetuksenHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.aineopiskeluperusopetus);
      case lukio:
        return lukioHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.lukio);
      case aineopiskelulukio:
        return lukioHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.aineopiskelulukio);
      case aikuistenperusopetuksenalkuvaihe:
        return apaHandler.studentToModel(student, settings.getAcademyIdentifier());

      default:
        logger.log(Level.WARNING, String.format("Student %d with studyprogramme %s was not reported to Koski because no handler was specified.", 
            student.getId(), student.getStudyProgramme().getName()));
        return null;
    }
  }
  
}
