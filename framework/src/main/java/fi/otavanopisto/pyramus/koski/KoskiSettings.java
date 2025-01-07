package fi.otavanopisto.pyramus.koski;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.settings.KoskiIntegrationSettingsWrapper;
import fi.otavanopisto.pyramus.koski.settings.StudyEndReasonMapping;
import net.sf.json.JSONObject;

@ApplicationScoped
public class KoskiSettings {

  // Global switch for Koski integration
  private static final String KOSKI_SETTINGKEY_ENABLED = "koski.enabled";
  // For test environment the saving of oid's needs to be turned off
  private static final String KOSKI_SETTINGKEY_TESTENVIRONMENT = "koski.testEnvironment";

  // Skipped from integration
  private static final String KOSKI_SKIPPED_STUDENT = KoskiConsts.VariableNames.KOSKI_SKIPPED_STUDENT;

  @Inject
  private Logger logger;

  @Inject
  private SettingDAO settingDAO;

  @Inject
  private SettingKeyDAO settingKeyDAO;

  @Inject
  private UserVariableDAO userVariableDAO;
  
  @Inject
  private UserVariableKeyDAO userVariableKeyDAO;
  
  @PostConstruct
  private void postConstruct() {
    testEnvironment = Boolean.parseBoolean(getSetting(KOSKI_SETTINGKEY_TESTENVIRONMENT));
    
    String fileName = System.getProperty("jboss.server.config.dir") + "/koski-config.json";
    File file = new File(fileName);
    if (file.exists()) {
      try {
        String json = FileUtils.readFileToString(file);
        
        ObjectMapper mapper = new ObjectMapper();
        this.settings = mapper.readValue(json, KoskiIntegrationSettingsWrapper.class);
        
        JSONObject settings = JSONObject.fromObject(json);
        readSettings(settings);
      } catch (IOException e) {
        logger.log(Level.SEVERE, String.format("Exception while reading Koski-integration configuration file %s.", fileName), e);
      }
    } else {
      logger.log(Level.SEVERE, String.format("Koski-integration configuration file %s couldn't be loaded.", fileName));
    }
  }
  
  private void readSettings(JSONObject settings) {
    JSONObject koskiSettings = settings.getJSONObject("koski");
    JSONObject courseTypeMapping = koskiSettings.getJSONObject("courseTypeMapping");
    for (Object courseTypeMappingKey : courseTypeMapping.keySet()) {
      Long courseSubTypeId = Long.parseLong(courseTypeMappingKey.toString());
      this.courseTypeMapping.put(courseSubTypeId, courseTypeMapping.getString(courseSubTypeId.toString()));
    }
  
    JSONObject courseTypeMapping2019 = koskiSettings.getJSONObject("courseTypeMapping2019");
    for (Object courseTypeMappingKey : courseTypeMapping2019.keySet()) {
      Long courseSubTypeId = Long.parseLong(courseTypeMappingKey.toString());
      this.courseTypeMapping2019.put(courseSubTypeId, courseTypeMapping2019.getString(courseSubTypeId.toString()));
    }
  
    JSONObject subjectToLanguageMapping = koskiSettings.getJSONObject("subjectToLanguageMapping");
    for (Object key : subjectToLanguageMapping.keySet()) {
      String keyStr = (String) key;
      this.subjectToLanguageMapping.put(keyStr, subjectToLanguageMapping.getString(keyStr));
    }
    
    JSONObject studyProgrammeMappings = koskiSettings.getJSONObject("studyProgrammes");
    for (Object studyProgrammeKey : studyProgrammeMappings.keySet()) {
      Long studyProgrammeId = Long.parseLong(studyProgrammeKey.toString());
      JSONObject studyProgramme = studyProgrammeMappings.getJSONObject(studyProgrammeId.toString());
      
      if (studyProgramme.getBoolean("enabled")) {
        enabledStudyProgrammes.add(studyProgrammeId);
      }
      
      if (studyProgramme.getBoolean("freeLodging")) {
        freeLodgingStudyProgrammes.add(studyProgrammeId);
      }
      
      if (studyProgramme.has("yksityisopiskelija") && studyProgramme.getBoolean("yksityisopiskelija")) {
        yksityisopiskelijaStudyProgrammes.add(studyProgrammeId);
      }
      
      if (EnumUtils.isValidEnum(KoskiStudyProgrammeHandler.class, studyProgramme.getString("handler"))) {
        KoskiStudyProgrammeHandler handlerType = KoskiStudyProgrammeHandler.valueOf(studyProgramme.getString("handler"));
        handlerTypes.put(studyProgrammeId, handlerType);
      } else {
        logger.warning(String.format("Unknown handler %s", studyProgramme.getString("handler")));
      }
      
      if (studyProgramme.has("opintojenRahoitus")) {
        String opintojenRahoitusStr = studyProgramme.getString("opintojenRahoitus");
        if (StringUtils.isNotBlank(opintojenRahoitusStr)) {
          OpintojenRahoitus or = OpintojenRahoitus.reverseLookup(opintojenRahoitusStr);
          if (or != null) {
            opintojenRahoitus.put(studyProgrammeId, or);
          }
        }
      }
      
      if (studyProgramme.has("perusopetuksenSuoritusTapa")) {
        String pstString = studyProgramme.getString("perusopetuksenSuoritusTapa");
        if (EnumUtils.isValidEnum(PerusopetuksenSuoritusTapa.class, pstString)) {
          PerusopetuksenSuoritusTapa pst = EnumUtils.getEnum(PerusopetuksenSuoritusTapa.class, pstString);
          if (pst != null) {
            perusopetuksenSuoritusTapa.put(studyProgrammeId, pst);
          }
        }
      }

      if (studyProgramme.has("toimipisteOID")) {
        String toimipisteOID = studyProgramme.getString("toimipisteOID");
        if (StringUtils.isNotBlank(toimipisteOID)) {
          toimipisteOIDt.put(studyProgrammeId, toimipisteOID);
        }
      }

      JSONObject diaariJSON = studyProgramme.getJSONObject("diaari");
      if (diaariJSON != null) {
        for (Object curriculumIdKey : diaariJSON.keySet()) {
          Long curriculumId = Long.parseLong(curriculumIdKey.toString());
          diaarinumerot.put(String.format("%d.%d", studyProgrammeId, curriculumId), diaariJSON.getString(curriculumIdKey.toString()));
        }
      }
      
      String pakollisetOppiaineet = studyProgramme.getString("pakollisetOppiaineet");
      if (StringUtils.isNotBlank(pakollisetOppiaineet)) {
        Set<KoskiOppiaineetYleissivistava> set = new HashSet<>();
        for (String s : StringUtils.split(pakollisetOppiaineet, ',')) {
          KoskiOppiaineetYleissivistava oppiaine = KoskiOppiaineetYleissivistava.valueOf(s);
          if (oppiaine != null) {
            set.add(oppiaine);
          } else {
            logger.log(Level.WARNING, String.format("No equivalent enum found for value %s of studyprogramme %d.", s, studyProgrammeId));
          }
        }
        this.pakollisetOppiaineet.put(studyProgrammeId, set);
      }
    }
  }

  public boolean isEnabled() {
    return Boolean.parseBoolean(getSetting(KOSKI_SETTINGKEY_ENABLED));
  }

  public boolean isTestEnvironment() {
    return testEnvironment;
  }

  public boolean isEnabledStudyProgramme(StudyProgramme studyProgramme) {
    if (studyProgramme == null)
      return false;
    
    return enabledStudyProgrammes.contains(studyProgramme.getId());
  }
  
  public OpiskelijanOPS resolveOPS(Student student) {
    Curriculum curriculum = student.getCurriculum();
    if (curriculum != null) {
      return resolveOPS(curriculum.getId().intValue());
    }
    return null;
  }
  
  public OpiskelijanOPS resolveOPS(Long curriculumId) {
    return resolveOPS(curriculumId.intValue());
  }
  
  public OpiskelijanOPS resolveOPS(Curriculum curriculum) {
    if (curriculum != null) {
      return resolveOPS(curriculum.getId().intValue());
    } else {
      return null;
    }
  }
  
  public OpiskelijanOPS resolveOPS(int curriculumId) {
    switch (curriculumId) {
      case 1:
        return OpiskelijanOPS.ops2016;
      case 2:
        return OpiskelijanOPS.ops2005;
      case 3:
        return OpiskelijanOPS.ops2018;
      case 4:
        return OpiskelijanOPS.ops2019;
    }
    
    logger.log(Level.WARNING, String.format("Unknown ops with id %d", curriculumId));
    return null;
  }

  @Deprecated
  public boolean isFreeLodging(Long studyProgrammeId) {
    return freeLodgingStudyProgrammes.contains(studyProgrammeId);
  }
  
  @Deprecated
  public boolean isYksityisopiskelija(Long studyProgrammeId) {
    return yksityisopiskelijaStudyProgrammes.contains(studyProgrammeId);
  }
  
  private String getSetting(String settingName) {
    SettingKey key = settingKeyDAO.findByName(settingName);
    if (key != null) {
      Setting setting = settingDAO.findByKey(key);
      
      if (setting != null) {
        return setting.getValue();
      }
    }
    
    return null;
  }

  /**
   * Is this credit such that it is being reported to the system
   * 
   * @param ca
   * @return
   */
  public boolean isReportedCredit(CreditStub ca) {
    return true;
  }

  public boolean hasReportedStudents(Person person) {
    return person.getStudents().stream().anyMatch((Student s) -> isReportedStudent(s));
  }
  
  public boolean isReportedStudent(Student student) {
    return isEnabledStudyProgramme(student.getStudyProgramme()) && !isSkippedStudent(student);
  }
  
  public boolean isSkippedStudent(Student student) {
    UserVariableKey variableKey = userVariableKeyDAO.findByVariableKey(KOSKI_SKIPPED_STUDENT);
    if (variableKey != null) {
      UserVariable userVariable = userVariableDAO.findByUserAndVariableKey(student, variableKey);
      if (userVariable != null) {
        String skippedStudentVariable = userVariable.getValue();
        return Boolean.valueOf(skippedStudentVariable) || StringUtils.equals(skippedStudentVariable, "1");
      } else {
        return false;
      }
    }
    
    return false;
  }

  public String getDiaariNumero(Long studyProgrammeId, Long curriculumId) {
    String key = String.format("%d.%d", studyProgrammeId, curriculumId);
    return diaarinumerot.get(key);
  }

  public Set<KoskiOppiaineetYleissivistava> getPakollisetOppiaineet(Long studyProgrammeId) {
    return pakollisetOppiaineet.get(studyProgrammeId);
  }
  
  public String getAcademyIdentifier() {
    return settings.getKoski().getAcademyIdentifier();
  }

  public String getCourseTypeMapping(Long educationSubTypeId) {
    return courseTypeMapping.get(educationSubTypeId);
  }
  
  public String getCourseTypeMapping2019(Long educationSubTypeId) {
    return courseTypeMapping2019.get(educationSubTypeId);
  }
  
  public String getSubjectToLanguageMapping(String subjectCode) {
    return subjectToLanguageMapping.get(subjectCode);
  }

  public KoskiStudyProgrammeHandler getStudyProgrammeHandlerType(Long studyProgrammeId) {
    return handlerTypes.get(studyProgrammeId);
  }

  public KoskiIntegrationSettingsWrapper getSettings() {
    return settings;
  }
  
  public String getToimipisteOID(Long studyProgrammeId, String defaultIdentifier) {
    return toimipisteOIDt.containsKey(studyProgrammeId) ?
        toimipisteOIDt.get(studyProgrammeId) : defaultIdentifier;
  }
  
  public OpintojenRahoitus getOpintojenRahoitus(Long studyProgrammeId) {
    return opintojenRahoitus.get(studyProgrammeId);
  }

  public StudyEndReasonMapping getStudyEndReasonMapping(StudentStudyEndReason studyEndReason) {
    return studyEndReason != null ? getSettings().getKoski().getStudyEndReasonMapping(studyEndReason.getId()) : null;
  }

  public PerusopetuksenSuoritusTapa getSuoritusTapa(Long studyProgrammeId, PerusopetuksenSuoritusTapa defaultValue) {
    return perusopetuksenSuoritusTapa.containsKey(studyProgrammeId) 
        ? perusopetuksenSuoritusTapa.get(studyProgrammeId) : defaultValue;
  }
  
  public String getVirkailijaUrl() {
    return getSetting(KoskiConsts.Setting.KOSKI_SETTINGKEY_VIRKAILIJAURL);
  }
  
  private KoskiIntegrationSettingsWrapper settings;
  private boolean testEnvironment;
  private Set<Long> enabledStudyProgrammes = new HashSet<Long>();
  private Set<Long> freeLodgingStudyProgrammes = new HashSet<Long>();
  @Deprecated private Set<Long> yksityisopiskelijaStudyProgrammes = new HashSet<Long>();
  private Map<Long, KoskiStudyProgrammeHandler> handlerTypes = new HashMap<>();
  private Map<Long, OpintojenRahoitus> opintojenRahoitus = new HashMap<>();
  private Map<Long, PerusopetuksenSuoritusTapa> perusopetuksenSuoritusTapa = new HashMap<>();
  private Map<Long, String> toimipisteOIDt = new HashMap<>();
  private Map<String, String> diaarinumerot = new HashMap<>();
  private Map<Long, String> courseTypeMapping = new HashMap<>();
  private Map<Long, String> courseTypeMapping2019 = new HashMap<>();
  private Map<String, String> subjectToLanguageMapping = new HashMap<>();
  private Map<Long, Set<KoskiOppiaineetYleissivistava>> pakollisetOppiaineet = new HashMap<>();
}
