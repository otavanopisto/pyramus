package fi.otavanopisto.pyramus;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;

/**
 * Initializes VariableKeys from PyramusConsts on Pyramus startup.
 * 
 * Only the variable key is created so they do not have any default values.
 * This could be later expanded on, maybe via CDI the variables could be 
 * defined as objects with annotations where the objects could include
 * default value and other properties as well.
 */
@Startup
@Singleton
public class PyramusVariableStartupInitializer {

  @Inject
  private SettingKeyDAO settingKeyDAO;
  
  @PostConstruct
  private void ensureVariableKeysExist() {
    ensureSettingKeyExists(PyramusConsts.Setting.TRANSFERCREDITIMPORTS_GRADINGSCALES_LUKIO);
  }
  
  private void ensureSettingKeyExists(String settingName) {
    if (settingKeyDAO.findByName(settingName) == null) {
      settingKeyDAO.create(settingName);
    }
  }
  
}
