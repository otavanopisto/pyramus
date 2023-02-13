package fi.otavanopisto.pyramus.koski;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

@Startup
@Singleton
public class KoskiStartupInitializer {

  @Inject
  private PersonVariableKeyDAO personVariableKeyDAO;

  @Inject
  private UserVariableKeyDAO userVariableKeyDAO;
  
  @Inject
  private SettingKeyDAO settingKeyDAO;
  
  @Inject
  private SchoolVariableKeyDAO schoolVariableKeyDAO;
  
  @PostConstruct
  private void ensureVariableKeysExist() {
    ensureSettingKeyExists(KoskiConsts.Setting.KOSKI_SETTINGKEY_AUTH);
    ensureSettingKeyExists(KoskiConsts.Setting.KOSKI_SETTINGKEY_BASEURL);
    ensureSettingKeyExists(KoskiConsts.Setting.KOSKI_SETTINGKEY_CSRF);
    
    if (personVariableKeyDAO.findByVariableKey(KoskiConsts.VariableNames.KOSKI_HENKILO_OID) == null) {
      personVariableKeyDAO.create(KoskiConsts.VariableNames.KOSKI_HENKILO_OID, "Koski: henkilön oid", VariableType.TEXT, true);
    }
    
    ensureUserVariableExists(KoskiConsts.VariableNames.KOSKI_STUDYPERMISSION_ID, "Koski: opiskeluoikeuden oid", VariableType.TEXT, true);
    ensureUserVariableExists(KoskiConsts.VariableNames.KOSKI_INTERNETIX_STUDYPERMISSION_ID, "Koski: aineopiskeluoikeuden oid", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.VariableNames.KOSKI_LINKED_STUDYPERMISSION_ID, "Koski: sisältyvän opiskeluoikeuden oid", VariableType.TEXT, true);
    ensureUserVariableExists(KoskiConsts.VariableNames.KOSKI_SKIPPED_STUDENT, "Ohita Koski-integraatiossa", VariableType.TEXT, true);

    ensureUserVariableExists(KoskiConsts.UserVariables.STARTED_UNDER18, "Aloittanut lukio-op. alle 18-vuotiaana", VariableType.BOOLEAN, true);
    ensureUserVariableExists(KoskiConsts.UserVariables.UNDER18_STARTREASON, "Alle 18-vuotiaana aloittaneen aloittamissyy", VariableType.TEXT, true);
    ensureUserVariableExists(KoskiConsts.UserVariables.PK_GRADE_UPGRADE, "Perusopetuksen arvosanojen korottaja", VariableType.BOOLEAN, true);

    if (schoolVariableKeyDAO.findVariableKey(KoskiConsts.SchoolVariables.KOSKI_SCHOOL_OID) == null) {
      schoolVariableKeyDAO.create(KoskiConsts.SchoolVariables.KOSKI_SCHOOL_OID, "Koski: oppilaitoksen OID", VariableType.TEXT, true);
    }
    
    // Subject Selections
    
    ensureUserVariableExists(KoskiConsts.SubjectSelections.MATH, "Matematiikka", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.NATIVE_LANGUAGE, "Äidinkieli", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.LANG_A, "A-kieli", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.LANG_A1, "A1-kieli", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.LANG_A2, "A2-kieli", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.LANG_B1, "B1-kieli", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.LANG_B2, "B2-kieli", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.LANG_B3, "B3-kieli", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.RELIGION, "Uskonto", VariableType.TEXT, false);
    ensureUserVariableExists(KoskiConsts.SubjectSelections.COMPLETION_MARKS, "S-merkintä", VariableType.TEXT, false);
  }
  
  private void ensureUserVariableExists(String key, String description, VariableType variableType, Boolean userEditable) {
    if (userVariableKeyDAO.findByVariableKey(key) == null) {
      userVariableKeyDAO.create(key, description, variableType, userEditable);
    }
  }
  
  private void ensureSettingKeyExists(String settingName) {
    if (settingKeyDAO.findByName(settingName) == null) {
      settingKeyDAO.create(settingName);
    }
  }
  
}
