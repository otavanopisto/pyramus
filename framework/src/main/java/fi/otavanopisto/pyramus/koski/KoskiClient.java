package fi.otavanopisto.pyramus.koski;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
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
import fi.otavanopisto.pyramus.koski.model.Oppija;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * https://dev.koski.opintopolku.fi/koski/documentation
 */
@ApplicationScoped
public class KoskiClient {

  private static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
  private static final String KOSKI_HENKILO_OID = "koski.henkilo-oid";
  
  private static final String KOSKI_SETTINGKEY_BASEURL = "koski.baseUrl";
  private static final String KOSKI_SETTINGKEY_AUTH = "koski.auth";
  private static final String KOSKI_SETTINGKEY_ACADEMYIDENTIFIER = "koski.academyIdentifier";
  
  public static KoskiClient getInstance() {
    return CDI.current().select(KoskiClient.class).get();
  }

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
  
  public void updateStudent(Student student) {
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
    String studyOid = userVariableDAO.findByUserAndKey(student, KOSKI_STUDYPERMISSION_ID);
    
    String uri = String.format("%s/oppija", getBaseUrl());
    try {
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target(uri);
      Builder request = target
          .request(MediaType.APPLICATION_JSON_TYPE)
          .header("Authorization", "Basic " + getAuth());
        
      StringWriter writer = new StringWriter();
      
      OpiskeluoikeudenTyyppi opiskeluoikeudenTyyppi = settings.getOpiskeluoikeudenTyyppi(student.getStudyProgramme().getId());
      
      Oppija oppija;
      
      switch (opiskeluoikeudenTyyppi) {
        case aikuistenperusopetus:
          oppija = aikuistenPerusopetuksenHandler.studentToModel(student, personOid, studyOid, getAcademyIdentifier());
        break;
        case lukiokoulutus:
          oppija = lukioHandler.studentToModel(student, personOid, studyOid, getAcademyIdentifier());
        break;
        
        default:
          logger.log(Level.WARNING, String.format("Student %d with studyprogramme %s was not reported to Koski because no handler was specified.", 
              student.getId(), student.getStudyProgramme().getName()));
          return;
      }
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      mapper.writeValue(writer, oppija);
      
      String requestStr = writer.toString();
      System.out.println("Request: " + requestStr);

      Response response = request.put(Entity.json(requestStr));
      String ret = response.readEntity(String.class);
      if (response.getStatus() == 200) {
        JSONObject returnValue = JSONObject.fromObject(ret);
        
        String servedPersonOid = JSONHelper.getString(returnValue, "henkilö.oid");
        
        JSONArray studies = returnValue.getJSONArray("opiskeluoikeudet");
        JSONObject study1 = studies.getJSONObject(0);
        String servedStudyOid = study1.getString("oid");

        if (StringUtils.isEmpty(personOid)) {
          // If the oid was empty in db, save the given one
          personVariableDAO.setPersonVariable(student.getPerson(), KOSKI_HENKILO_OID, servedPersonOid);
        } else {
          // Validate the oid is the same
          if (!StringUtils.equals(personOid, servedPersonOid))
            throw new RuntimeException("Returned person oid doesn't match the saved oid");
        }

        if (StringUtils.isBlank(studyOid)) {
          userVariableDAO.setUserVariable(student, KOSKI_STUDYPERMISSION_ID, servedStudyOid);
        } else {
          // Validate the oid is the same
          if (!StringUtils.equals(studyOid, servedStudyOid))
            throw new RuntimeException("Returned person oid doesn't match the saved oid");
        }

        // {"henkilö":{"oid":"1.2.246.562.24.00000000038"},"opiskeluoikeudet":[{"id":151,"versionumero":1}]}
      } else
        logger.log(Level.SEVERE, String.format("Koski server returned %d when trying to create student. Content %s", response.getStatus(), ret));
        
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Koski oid of an academy
   * @return
   */
  public String getAcademyIdentifier() {
    return getSetting(KOSKI_SETTINGKEY_ACADEMYIDENTIFIER);
  }
  
}
