package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.system.SettingDAO;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.domainmodel.system.Setting;
import fi.pyramus.domainmodel.system.SettingKey;

public class SettingAPI {

  public SettingAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(Long settingKeyId, String value){
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKey settingKey = DAOFactory.getInstance().getSettingKeyDAO().findById(settingKeyId);
    Setting setting = settingDAO.create(settingKey, value);
    return setting.getId();
  }
  
  public String get(String key) {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    
    SettingKey settingKey = settingKeyDAO.findByName(key);
    if (settingKey == null) {
      return null;
    }
    
    Setting setting = settingDAO.findByKey(settingKey);
    if (setting != null) {
      return setting.getValue();
    }
    
    return null;
  }
  
  public void set(String key, String value) {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    
    SettingKey settingKey = settingKeyDAO.findByName(key);
    if (settingKey == null) {
      settingKey = settingKeyDAO.create(key);
    }
    
    Setting setting = settingDAO.findByKey(settingKey);
    if (setting == null) {
      settingDAO.create(settingKey, value);
    } else {
      settingDAO.update(setting, settingKey, value);
    }
  }
  
  @SuppressWarnings("unused")
  private Long loggedUserId;
}
