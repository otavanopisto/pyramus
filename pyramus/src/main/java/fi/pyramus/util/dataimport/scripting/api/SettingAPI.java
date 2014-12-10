package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.system.SettingDAO;
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
  
  @SuppressWarnings("unused")
  private Long loggedUserId;
}
