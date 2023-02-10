package fi.otavanopisto.pyramus.koski;

import java.io.StringWriter;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
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

import org.apache.commons.collections.CollectionUtils;
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
import fi.otavanopisto.pyramus.koski.exception.NoLatestStudentException;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusJakso;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.result.KoskiErrorMessageBody;
import fi.otavanopisto.pyramus.koski.model.result.OpiskeluoikeusReturnVal;
import fi.otavanopisto.pyramus.koski.model.result.OppijaReturnVal;

/**
 * https://dev.koski.opintopolku.fi/koski/documentation
 */
@ApplicationScoped
public class KoskiClient {

  private static final String KOSKI_HENKILO_OID = KoskiConsts.VariableNames.KOSKI_HENKILO_OID;
  
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
  private KoskiController koskiController;
  
  private String getBaseUrl() {
    return getSetting(KoskiConsts.Setting.KOSKI_SETTINGKEY_BASEURL);
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
    try {
      Builder request = prepareRequest(client, uri);
      OppijaReturnVal returnVal = request.get(OppijaReturnVal.class);
      return returnVal;
    } finally {
      client.close();
    }
  }

  /**
   * Loads and parses the whole person object from Koski.
   */
  public Oppija findOppijaByOid(String oppijaOid) throws Exception {
    String uri = String.format("%s/oppija/%s", getBaseUrl(), oppijaOid);
    
    Client client = ClientBuilder.newClient();
    try {
      Builder request = prepareRequest(client, uri);
      Oppija oppija = request.get(Oppija.class);
      return oppija;
    } finally {
      client.close();
    }
  }

  /**
   * Invalidates a study permit in Koski.
   */
  public boolean invalidateStudyOid(Person person, String studyPermitOid) throws Exception {
    return invalidateStudyOid(person, Arrays.asList(studyPermitOid));
  }
  
  /**
   * Invalidates a set of study permits in Koski.
   */
  public boolean invalidateStudyOid(Person person, Collection<String> studyPermitOids) throws Exception {
    if (CollectionUtils.isEmpty(studyPermitOids)) {
      logger.log(Level.INFO, String.format("Invalidation called with no oids for person %d", person.getId()));
      return true;
    }
    
    logger.log(Level.INFO, String.format("Invalidating OIDs %s for person %d", studyPermitOids, person.getId()));
    
    String oppijaOid = personVariableDAO.findByPersonAndKey(person, KOSKI_HENKILO_OID);
    Oppija oppija = findOppijaByOid(oppijaOid);

    // Remove non-compatible entities
    oppija.getOpiskeluoikeudet().removeIf(opiskeluoikeus -> (opiskeluoikeus == null || opiskeluoikeus.getOid() == null));
    oppija.getOpiskeluoikeudet().removeIf(opiskeluoikeus -> getLahdejarjestelma(opiskeluoikeus) != Lahdejarjestelma.pyramus);
    
    long matchingOIDs = oppija.getOpiskeluoikeudet().stream()
        .filter(opiskeluoikeus -> opiskeluoikeus.getOid() != null)
        .filter(opiskeluoikeus -> getLahdejarjestelma(opiskeluoikeus) == Lahdejarjestelma.pyramus)
        .filter(opiskeluoikeus -> studyPermitOids.contains(opiskeluoikeus.getOid()))
        .count();

    if (matchingOIDs == 0) {
      logger.log(Level.WARNING, String.format("No matching OIDs were found (Person %d). Aborting.", person.getId()));
      return false;
    }
    
    if (matchingOIDs != studyPermitOids.size()) {
      logger.log(Level.WARNING, String.format("Specified OID(s) were not present (Person %d). Aborting.", person.getId()));
      return false;
    }
    
    // Remove study permits that don't have matching oid
    oppija.getOpiskeluoikeudet().removeIf(opiskeluoikeus -> !studyPermitOids.contains(opiskeluoikeus.getOid()) || getLahdejarjestelma(opiskeluoikeus) != Lahdejarjestelma.pyramus);
    
    // There were n matching OIDs that should still be present there (just to be as certain about removing just the wanted ones as possible) 
    if (oppija.getOpiskeluoikeudet().size() == matchingOIDs) {
      for (Opiskeluoikeus opiskeluoikeus : oppija.getOpiskeluoikeudet()) {
        if (getLahdejarjestelma(opiskeluoikeus) == Lahdejarjestelma.pyramus) {
          SourceSystemId sourceSystemId = parseSource(opiskeluoikeus.getLahdejarjestelmanId().getId());
          if (sourceSystemId != null) {
            // Having multiple periods is going to cause problems invalidating due to date checks in Koski
            opiskeluoikeus.getTila().getOpiskeluoikeusjaksot().clear();
            
            Date invalidationDate = opiskeluoikeus.getPaattymispaiva() != null ? opiskeluoikeus.getPaattymispaiva() : 
              opiskeluoikeus.getAlkamispaiva() != null ? opiskeluoikeus.getAlkamispaiva() : new Date();
            opiskeluoikeus.getTila().addOpiskeluoikeusJakso(
                new OpiskeluoikeusJakso(invalidationDate, OpiskeluoikeudenTila.mitatoity));
          } else {
            String emsg = String.format("Could not invalidate student oid because returned source system id couldn't be parsed (Person %d).", person.getId());
            logger.log(Level.WARNING, emsg);
            koskiPersonLogDAO.create(person, KoskiPersonState.UNKNOWN_FAILURE, new Date(), emsg);
            return false;
          }
        } else {
          String emsg = String.format("Could not invalidate student oid because source system is not Pyramus (Person %d).", person.getId());
          logger.log(Level.WARNING, emsg);
          koskiPersonLogDAO.create(person, KoskiPersonState.UNKNOWN_FAILURE, new Date(), emsg);
          return false;
        }
      }
      
      return updatePersonToKoski(oppija, person, oppijaOid);
    } else {
      String emsg = String.format("Unexpected error filtering study permits for invalidation (person=%d, n=%d).", person.getId(), matchingOIDs);
      logger.log(Level.WARNING, emsg);
      koskiPersonLogDAO.create(person, KoskiPersonState.UNKNOWN_FAILURE, new Date(), emsg);
    }
    
    return false;
  }

  public Lahdejarjestelma getLahdejarjestelma(Opiskeluoikeus opiskeluoikeus) {
    return (opiskeluoikeus != null && opiskeluoikeus.getLahdejarjestelmanId() != null && opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma() != null)
        ? opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma().getValue() : null;
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
            
      Oppija oppija = koskiController.personToOppija(person);
      
      if (oppija.getOpiskeluoikeudet().size() == 0) {
        logger.info(String.format("Updating person %d was skipped due to no updateable study permits.", person.getId()));
        return;
      }
      
      updatePersonToKoski(oppija, person, personOid);
    } catch (NoLatestStudentException nlse) {
      logger.severe(String.format("Could not resolve latest student for person %d.", person.getId()));
      koskiPersonLogDAO.create(person, KoskiPersonState.UNKNOWN_FAILURE, new Date());
    } catch (Exception ex) {
      logger.log(Level.SEVERE, String.format("Unknown error while processing person %d", person != null ? person.getId() : null), ex);
      koskiPersonLogDAO.create(person, KoskiPersonState.UNKNOWN_FAILURE, new Date(), ex.getMessage());
    }
  }
  
  private boolean updatePersonToKoski(Oppija oppija, Person person, String personOid) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      StringWriter writer = new StringWriter();
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      mapper.writeValue(writer, oppija);
      
      String requestStr = writer.toString();

      String uri = String.format("%s/oppija", getBaseUrl());

      Client client = ClientBuilder.newClient();
      try {
        Response response = prepareRequest(client, uri).put(Entity.json(requestStr));
  
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
  
            Set<SourceSystemId> invalidatedSourceSystemIds = oppija.getOpiskeluoikeudet().stream()
              .filter(opiskeluoikeus -> opiskeluoikeus.getTila().getOpiskeluoikeusjaksot().stream().anyMatch(opiskeluoikeusjakso -> opiskeluoikeusjakso.getTila().getValue() == OpiskeluoikeudenTila.mitatoity))
              .map(opiskeluoikeus -> parseSource(opiskeluoikeus.getLahdejarjestelmanId().getId()))
              .collect(Collectors.toSet());
            
            for (OpiskeluoikeusReturnVal opiskeluoikeus : oppijaReturnVal.getOpiskeluoikeudet()) {
              if (opiskeluoikeus.getLahdejarjestelmanId() != null && 
                  opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma() != null &&
                  StringUtils.equals(opiskeluoikeus.getLahdejarjestelmanId().getLahdejarjestelma().getKoodiarvo(), "pyramus")) {
                SourceSystemId sourceSystemId = parseSource(opiskeluoikeus.getLahdejarjestelmanId().getId());
                if (sourceSystemId != null) {
                  Student reportedStudent = studentDAO.findById(sourceSystemId.getStudentId());
                  
                  KoskiStudentHandler handler = koskiController.getStudentHandler(sourceSystemId.getHandler());
                  if (reportedStudent.getArchived() || invalidatedSourceSystemIds.contains(sourceSystemId)) {
                    // For archived or invalidated student the studypermission oid is cleared as Koski doesn't want to receive this id ever again
                    handler.removeOid(sourceSystemId.getHandler(), reportedStudent, opiskeluoikeus.getOid());
                  } else {
                    handler.saveOrValidateOid(sourceSystemId.getHandler(), reportedStudent, opiskeluoikeus.getOid());
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
      } finally {
        client.close();
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

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof SourceSystemId) {
        SourceSystemId comp = (SourceSystemId) obj;
        return this.handler == comp.handler && Objects.equals(this.studentId, comp.studentId);
      } else {
        return false;
      }
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(handler, studentId);
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

  public boolean invalidateAllStudentOIDs(Student student) throws Exception {
    if (settings.isEnabledStudyProgramme(student.getStudyProgramme())) {
      Set<String> studentOIDs = koskiController.listStudentOIDs(student);
      return invalidateStudyOid(student.getPerson(), studentOIDs);
    } else {
      return false;
    }
  }

  private Builder prepareRequest(Client client, String uri) {
    String auth = getSetting(KoskiConsts.Setting.KOSKI_SETTINGKEY_AUTH);
    WebTarget target = client.target(uri);
    Builder request = target
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Authorization", "Basic " + auth);
    
    String csrf = getSetting(KoskiConsts.Setting.KOSKI_SETTINGKEY_CSRF);
    if (StringUtils.isNotBlank(csrf)) {
      request
        .header("CSRF", csrf)
        .cookie("CSRF", csrf);
    }
    
    return request;
  }

}
