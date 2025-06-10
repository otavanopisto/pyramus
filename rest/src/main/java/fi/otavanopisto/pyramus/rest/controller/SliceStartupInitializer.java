package fi.otavanopisto.pyramus.rest.controller;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

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
