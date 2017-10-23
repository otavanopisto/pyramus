package fi.otavanopisto.pyramus.koski;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Henkilo;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.result.OppijaReturnVal;

/**
 * https://dev.koski.opintopolku.fi/koski/documentation
 */
//@ApplicationScoped
@RequestScoped
public class KoskiClient {

  private static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
  private static final String KOSKI_HENKILO_OID = "koski.henkilo-oid";
  
  private static final String KOSKI_SETTINGKEY_BASEURL = "koski.baseUrl";
  private static final String KOSKI_SETTINGKEY_AUTH = "koski.auth";
  
//  public static KoskiClient getInstance() {
//    return CDI.current().select(KoskiClient.class).get();
//  }

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
  private UserVariableDAO userVariableDAO;

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
  
  public void findStudentByOID(String oid) {
    String uri = String.format("%s/oppija/%s", getBaseUrl(), oid);
    
    try {
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target(uri);
      Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      request.header("Authorization", "Basic " + getAuth());
      request.accept(MediaType.APPLICATION_JSON_TYPE);
      
  //    Response response = request.get();
    
      String ret = request.get(String.class);
      System.out.println(ret);
      
      
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public void searchStudent(String query) {
    String uri = String.format("%s/henkilo/search?query=%s", getBaseUrl(), query);
  
    try {
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target(uri);
      Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      request.header("Authorization", "Basic " + getAuth());
      
  //    Response response = request.get();
    
      String ret = request.get(String.class);
      System.out.println(ret);
      
      
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private <T> T createResponse(Response response, Class<T> type) {
    return response.readEntity(type);
  }
  
  public void findStudentBySSN(String ssn) {
    String uri = String.format("%s/henkilo/hetu/%s", getBaseUrl(), ssn);

    try {
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target(uri);
      Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      request.header("Authorization", "Basic " + getAuth());
      
      Response response = request.get();
      String ret = createResponse(response, String.class);
      System.out.println(ret);
      
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public void updateStudent(Student student) throws KoskiException {
    if (!settings.isEnabled())
      return;
    
    if (student == null) {
      logger.log(Level.WARNING, "updateStudent called with null student.");
      return;
    }
    
    if (student.getStudyProgramme() == null) {
      logger.log(Level.WARNING, String.format("Can not update student (%d) without studyprogramme.", student.getId()));
      return;
    }

    if (!settings.isEnabledStudyProgramme(student.getStudyProgramme().getId()))
      return;
    
    String personOid = personVariableDAO.findByPersonAndKey(student.getPerson(), KOSKI_HENKILO_OID);
//    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);
    
    String uri = String.format("%s/oppija", getBaseUrl());
    try {
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target(uri);
      Builder request = target
          .request(MediaType.APPLICATION_JSON_TYPE)
          .header("Authorization", "Basic " + getAuth());
        
      StringWriter writer = new StringWriter();
      
      Henkilo henkilo;
      if (StringUtils.isNotBlank(personOid))
        henkilo = new HenkiloTiedotJaOID(personOid, student.getPerson().getSocialSecurityNumber(), student.getFirstName(), student.getLastName(), getCallname(student));
      else
        henkilo = new HenkiloUusi(student.getPerson().getSocialSecurityNumber(), student.getFirstName(), student.getLastName(), getCallname(student));

      Oppija oppija = new Oppija();
      oppija.setHenkilo(henkilo);
      
//      Oppija oppija;

      List<Student> reportedStudents = new ArrayList<>();
      
      for (Student s : student.getPerson().getStudents()) {
        Opiskeluoikeus o = studentToOpiskeluoikeus(s);
        if (o != null) {
          oppija.addOpiskeluoikeus(o);
          reportedStudents.add(s);
        }
      }
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      mapper.writeValue(writer, oppija);
      
      String requestStr = writer.toString();
      System.out.println("Request: " + requestStr);

      Response response = request.put(Entity.json(requestStr));
      String ret = response.readEntity(String.class);
      if (response.getStatus() == 200) {
        System.out.println("Response: " + ret);
        
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

          // TODO: käytä palautettuja lähdejärjestelmäid:tä
          if (oppijaReturnVal.getOpiskeluoikeudet().size() == reportedStudents.size()) {
            for (int i = 0; i < oppijaReturnVal.getOpiskeluoikeudet().size(); i++) {
              Student reportedStudent = reportedStudents.get(i);
              String studyOid = userVariableDAO.findByUserAndKey(reportedStudent, KOSKI_STUDYPERMISSION_ID);
              String servedStudyOid = oppijaReturnVal.getOpiskeluoikeudet().get(i).getOid();
                  
              if (StringUtils.isBlank(studyOid)) {
                userVariableDAO.setUserVariable(reportedStudent, KOSKI_STUDYPERMISSION_ID, servedStudyOid);
              } else {
                // Validate the oid is the same
                if (!StringUtils.equals(studyOid, servedStudyOid))
                  throw new RuntimeException(String.format("Returned study permit oid %s doesn't match the saved oid %s.", servedStudyOid, studyOid));
              }
            }
          } else {
            logger.log(Level.SEVERE, String.format("Koski server returned %d study permits while %d was sent.", 
                oppijaReturnVal.getOpiskeluoikeudet().size(), reportedStudents.size()));
          }
        }
      } else
        logger.log(Level.SEVERE, String.format("Koski server returned %d when trying to create student. Content %s", response.getStatus(), ret));
        
    } catch (Exception ex) {
      ex.printStackTrace();
    }
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

  private Opiskeluoikeus studentToOpiskeluoikeus(Student student) {
    OpiskeluoikeudenTyyppi opiskeluoikeudenTyyppi = settings.getOpiskeluoikeudenTyyppi(student.getStudyProgramme().getId());
    switch (opiskeluoikeudenTyyppi) {
      case aikuistenperusopetus:
        return aikuistenPerusopetuksenHandler.studentToModel(student, settings.getAcademyIdentifier());
      case lukiokoulutus:
        return lukioHandler.studentToModel(student, settings.getAcademyIdentifier());
      
      default:
        logger.log(Level.WARNING, String.format("Student %d with studyprogramme %s was not reported to Koski because no handler was specified.", 
            student.getId(), student.getStudyProgramme().getName()));
        return null;
    }
  }
  
}
