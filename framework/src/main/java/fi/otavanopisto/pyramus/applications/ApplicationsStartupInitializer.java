package fi.otavanopisto.pyramus.applications;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;

@Startup
@Singleton
public class ApplicationsStartupInitializer {

  @Inject
  private SettingDAO settingDAO;
  
  @Inject
  private SettingKeyDAO settingKeyDAO;
  
  @PostConstruct
  private void ensureVariableKeysExist() {
    /*
     * StudentParentRegistration enabled setting, set to true by default
     */
    String settingKeyName = ApplicationUtils.SETTINGKEY_STUDENTPARENTREGISTRATIONENABLED;
    if (settingKeyDAO.findByName(settingKeyName) == null) {
      SettingKey settingKey = settingKeyDAO.create(settingKeyName);
      settingDAO.create(settingKey, "true");
    }
  }
  
}
