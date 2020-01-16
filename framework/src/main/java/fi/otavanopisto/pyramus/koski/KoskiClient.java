package fi.otavanopisto.pyramus.koski;

import java.io.StringWriter;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.model.Henkilo;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.HenkiloUusi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.KoskiAikuistenPerusopetuksenStudentHandler;
import fi.otavanopisto.pyramus.koski.model.apa.KoskiAPAStudentHandler;
import fi.otavanopisto.pyramus.koski.model.internetix.KoskiInternetixStudentHandler;
import fi.otavanopisto.pyramus.koski.model.lukio.KoskiLukioStudentHandler;
import fi.otavanopisto.pyramus.koski.model.result.KoskiErrorMessageBody;
import fi.otavanopisto.pyramus.koski.model.result.OpiskeluoikeusReturnVal;
import fi.otavanopisto.pyramus.koski.model.result.OppijaReturnVal;

/**
 * https://dev.koski.opintopolku.fi/koski/documentation
 */
@ApplicationScoped
public class KoskiClient {

  private static final String KOSKI_HENKILO_OID = "koski.henkilo-oid";
  
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
  private KoskiPersonLogDAO koskiPersonLogDAO;
  
  @Inject
  private KoskiAPAStudentHandler apaHandler;

  @Inject
  private KoskiLukioStudentHandler lukioHandler;
  
  @Inject
  private KoskiInternetixStudentHandler internetixHandler;

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

  /**
   * 
   */
  public OppijaReturnVal findPersonByOid(String personOid) throws Exception {
    String uri = String.format("%s/oppija/%s", getBaseUrl(), personOid);
    
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(uri);
    Builder request = target
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Authorization", "Basic " + getAuth());

    return request.get(OppijaReturnVal.class);
  }

  /**
   * Loads and parses the whole person object from Koski.
   */
  public Oppija findOppijaByOid(String oppijaOid) throws Exception {
    String uri = String.format("%s/oppija/%s", getBaseUrl(), oppijaOid);
    
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(uri);
    Builder request = target
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Authorization", "Basic " + getAuth());

    return request.get(Oppija.class);
  }

  /**
   * Invalidates a study permit in Koski.
   */
  public void invalidateStudyOid(Person person, String studyPermitOid) throws Exception {
    String oppijaOid = personVariableDAO.findByPersonAndKey(person, KOSKI_HENKILO_OID);
    Oppija oppija = findOppijaByOid(oppijaOid);
    
    // Remove study permits that don't have matching oid
    oppija.getOpiskeluoikeudet().removeIf(opiskeluoikeus -> !StringUtils.equals(opiskeluoikeus.getOid(), studyPermitOid));
    
    // There should be the one study permit left to be invalidated
    if (oppija.getOpiskeluoikeudet().size() == 1) {
      Opiskeluoikeus opiskeluoikeus = oppija.getOpiskeluoikeudet().get(0);
      
      if (opiskeluoikeus.getLahdejarjestelmanId() != null &&
          opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma() != null &&
          opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma().getValue() == Lahdejarjestelma.pyramus) {
        SourceSystemId sourceSystemId = parseSource(opiskeluoikeus.getLahdejarjestelmanId().getId());
        if (sourceSystemId != null) {
          // Having multiple periods is going to cause problems invalidating due to date checks in Koski
          opiskeluoikeus.getTila().getOpiskeluoikeusjaksot().clear();
          
          Date invalidationDate = opiskeluoikeus.getPaattymispaiva() != null ? opiskeluoikeus.getPaattymispaiva() : 
            opiskeluoikeus.getAlkamispaiva() != null ? opiskeluoikeus.getAlkamispaiva() : new Date();
          opiskeluoikeus.getTila().addOpiskeluoikeusJakso(
              new OpiskeluoikeusJakso(invalidationDate, OpiskeluoikeudenTila.mitatoity));
        
          if (updatePersonToKoski(oppija, person, oppijaOid)) {
            Student reportedStudent = studentDAO.findById(sourceSystemId.getStudentId());
            KoskiStudentHandler handler = getHandlerType(sourceSystemId.getHandler());
            handler.saveOrValidateOid(sourceSystemId.getHandler(), reportedStudent, opiskeluoikeus.getOid());
          }
        } else {
          logger.log(Level.WARNING, String.format("Could not update student oid because returned source system id couldn't be parsed (Person %d).", person.getId()));
        }
      }
    }
  }

  /**
   * Updates Person to Koski.
   */
  public void updatePerson(Person person) {
    try {
      if (!settings.isEnabled())
        return;
      
      if (person == null) {
        logger.log(Level.WARNING, "updateStudent called with null person.");
        return;
      }
  
      clearPersonLog(person);
      
      // Does the person have any reported study programmes
      if (!settings.hasReportedStudents(person)) {
        return;
      }
  
      String personOid = personVariableDAO.findByPersonAndKey(person, KOSKI_HENKILO_OID);
      
      if (StringUtils.isBlank(person.getSocialSecurityNumber()) && StringUtils.isBlank(personOid)) {
        logger.warning(String.format("Can not update person (%d) without SSN or OID.", person.getId()));
        koskiPersonLogDAO.create(person, KoskiPersonState.NO_UNIQUE_ID, new Date());
        return;
      }
      
      Student latestStudent = person.getLatestStudent();
      
      if (latestStudent == null) {
        logger.severe(String.format("Could not resolve latest student for person %d.", person.getId()));
        koskiPersonLogDAO.create(person, KoskiPersonState.UNKNOWN_FAILURE, new Date());
        return;
      }
      
      Henkilo henkilo;
      if (StringUtils.isNotBlank(personOid))
        henkilo = new HenkiloTiedotJaOID(personOid, person.getSocialSecurityNumber(), latestStudent.getFirstName(), latestStudent.getLastName(), getCallname(latestStudent));
      else
        henkilo = new HenkiloUusi(person.getSocialSecurityNumber(), latestStudent.getFirstName(), latestStudent.getLastName(), getCallname(latestStudent));
  
      Oppija oppija = new Oppija();
      oppija.setHenkilo(henkilo);
      
      List<Student> reportedStudents = new ArrayList<>();
      
      for (Student s : person.getStudents()) {
        if (settings.isReportedStudent(s)) {
          List<Opiskeluoikeus> opiskeluoikeudet = studentToOpiskeluoikeus(s);
          for (Opiskeluoikeus o : opiskeluoikeudet) {
            if (o != null) {
              oppija.addOpiskeluoikeus(o);
              reportedStudents.add(s);
            }
          }
        }
      }
      
      if (oppija.getOpiskeluoikeudet().size() == 0) {
        logger.info(String.format("Updating person %d was skipped due to no updateable study permits.", person.getId()));
        return;
      }
      
      updatePersonToKoski(oppija, person, personOid);
    } catch (Exception ex) {
      logger.log(Level.SEVERE, String.format("Unknown error while processing person %d", person != null ? person.getId() : null), ex);
      koskiPersonLogDAO.create(person, KoskiPersonState.UNKNOWN_FAILURE, new Date(), ex.getMessage());
    }
  }
  
  private boolean updatePersonToKoski(Oppija oppija, Person person, String personOid) {
    try {
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
            personVariableDAO.setPersonVariable(person, KOSKI_HENKILO_OID, servedPersonOid);
          } else {
            // Validate the oid is the same
            if (!StringUtils.equals(personOid, servedPersonOid))
              throw new RuntimeException("Returned person oid doesn't match the saved oid");
          }

          for (OpiskeluoikeusReturnVal opiskeluoikeus : oppijaReturnVal.getOpiskeluoikeudet()) {
            if (opiskeluoikeus.getLahdejarjestelmanId() != null && 
                opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma() != null &&
                StringUtils.equals(opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma().getKoodiarvo(), "pyramus")) {
              SourceSystemId sourceSystemId = parseSource(opiskeluoikeus.getLahdejarjestelmanId().getId());
              if (sourceSystemId != null) {
                Student reportedStudent = studentDAO.findById(sourceSystemId.getStudentId());
                
                KoskiStudentHandler handler = getHandlerType(sourceSystemId.getHandler());
                if (!reportedStudent.getArchived()) {
                  handler.saveOrValidateOid(sourceSystemId.getHandler(), reportedStudent, opiskeluoikeus.getOid());
                } else {
                  // For archived student the studypermission oid is cleared as Koski doesn't want to receive this id ever again
                  handler.removeOid(sourceSystemId.getHandler(), reportedStudent, opiskeluoikeus.getOid());
                }
              } else {
                logger.log(Level.WARNING, String.format("Could not update student oid because returned source system id couldn't be parsed (Person %d).", person.getId()));
              }
            } else {
              logger.log(Level.WARNING, String.format("Could not update student oid because returned source system was not defined or isn't this system."));
            }
          }
        }

        // Log successful event
        koskiPersonLogDAO.create(person, KoskiPersonState.SUCCESS, new Date());
        logger.info(String.format("KoskiClient: successfully updated person %d.", person.getId()));
        return true;
      } else {
        String ret = response.readEntity(String.class);
        String errorMessage = errorResponseMessage(ret);
        
        // Log failed event
        koskiPersonLogDAO.create(person, KoskiPersonState.SERVER_FAILURE, new Date(), errorMessage);
        logger.log(Level.SEVERE, String.format("Koski server returned %d when trying to create person %d. Content %s", response.getStatus(), person.getId(), ret));
        return false;
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
        koskiPersonLogDAO.create(person, reason, new Date(), ex.getMessage());
      } catch (Exception e) {
      }
      
      logger.log(Level.SEVERE, String.format("Unknown failure while updating person %d", person.getId()), ex);
      return false;
    }
  }

  private String errorResponseMessage(String errorJSON) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      TypeReference<List<KoskiErrorMessageBody>> typeRef = new TypeReference<List<KoskiErrorMessageBody>>() {};
      List<KoskiErrorMessageBody> messageBodies = mapper.readValue(errorJSON, typeRef);
      Set<String> messages = messageBodies.stream().map(messageBody -> messageBody.getMessage()).filter(Objects::nonNull).collect(Collectors.toSet());
      return String.join(", ", messages);
    } catch (Exception e) {
      logger.log(Level.SEVERE, String.format("Couldn't parse error message %s", errorJSON), e);
    }
    
    return null;
  }

  public KoskiStudentHandler getHandlerType(KoskiStudyProgrammeHandler handler) {
    switch (handler) {
      case aikuistenperusopetus:
        return aikuistenPerusopetuksenHandler;
      case lukio:
        return lukioHandler;
      case aineopiskeluperusopetus:
      case aineopiskelulukio:
        return internetixHandler;
      case aikuistenperusopetuksenalkuvaihe:
        return apaHandler;

      default:
        logger.severe(String.format("Handler for type %s couldn't be determined.", handler));
        return null;
    }
  }

  class SourceSystemId {
    public SourceSystemId(KoskiStudyProgrammeHandler handler, Long studentId) {
      this.handler = handler;
      this.studentId = studentId;
    }
    
    public Long getStudentId() {
      return studentId;
    }

    public KoskiStudyProgrammeHandler getHandler() {
      return handler;
    }

    private final KoskiStudyProgrammeHandler handler;
    private final Long studentId;
  }
  
  private SourceSystemId parseSource(String lahdeJarjestelmaId) {
    if (StringUtils.contains(lahdeJarjestelmaId, ":")) {
      KoskiStudyProgrammeHandler handler = KoskiStudyProgrammeHandler.valueOf(StringUtils.substringBefore(lahdeJarjestelmaId, ":"));
      Long studentId = Long.valueOf(StringUtils.substringAfter(lahdeJarjestelmaId, ":"));
      return new SourceSystemId(handler, studentId);
    }
    
    return null;
  }

  private void clearPersonLog(Person person) {
    List<KoskiPersonLog> entries = koskiPersonLogDAO.listByPerson(person);
    entries.forEach(entry -> koskiPersonLogDAO.delete(entry));
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

  private List<Opiskeluoikeus> studentToOpiskeluoikeus(Student student) {
    KoskiStudyProgrammeHandler handler = settings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId());
    switch (handler) {
      case aikuistenperusopetus:
        return asList(aikuistenPerusopetuksenHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.aikuistenperusopetus));
      case lukio:
        return asList(lukioHandler.studentToModel(student, settings.getAcademyIdentifier(), KoskiStudyProgrammeHandler.lukio));
      case aineopiskeluperusopetus:
      case aineopiskelulukio:
        return internetixHandler.studentToModel(student, settings.getAcademyIdentifier());
      case aikuistenperusopetuksenalkuvaihe:
        return asList(apaHandler.studentToModel(student, settings.getAcademyIdentifier()));

      default:
        logger.log(Level.WARNING, String.format("Student %d with studyprogramme %s was not reported to Koski because no handler was specified.", 
            student.getId(), student.getStudyProgramme().getName()));
        return null;
    }
  }

  private <T> List<T> asList(T o) {
    List<T> list = new ArrayList<T>();
    list.add(o);
    return list;
  }
}
