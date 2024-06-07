package fi.otavanopisto.pyramus.rest.controller;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;

@Startup
@Singleton
public class SliceStartupInitializer {

  @Inject
  private SettingKeyDAO settingKeyDAO;
  
  @PostConstruct
  private void ensureVariableKeysExist() {
    String settingKeyName = SliceController.SLICEAUTH_SETTINGKEY;
    if (settingKeyDAO.findByName(settingKeyName) == null) {
      settingKeyDAO.create(settingKeyName);
    }
  }
  
}
