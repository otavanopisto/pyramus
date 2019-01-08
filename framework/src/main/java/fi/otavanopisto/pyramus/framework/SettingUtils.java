package fi.otavanopisto.pyramus.framework;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;

public class SettingUtils {
  
  public static String getSettingValue(String key) {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.findByName(key);
    if (settingKey != null) {
      SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null && setting.getValue() != null) {
        return setting.getValue();
      }
    }
    return null;
  }

  public static void setSettingValue(String key, String value) {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.findByName(key);
    if (settingKey == null) {
      settingKey = settingKeyDAO.create(key);
    }
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    Setting setting = settingDAO.findByKey(settingKey);
    if (setting == null) {
      settingDAO.create(settingKey, value);
    }
    else {
      settingDAO.update(setting, settingKey, value);
    }
  }

}
