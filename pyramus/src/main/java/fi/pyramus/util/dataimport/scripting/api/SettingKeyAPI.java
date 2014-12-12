package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.domainmodel.system.SettingKey;

public class SettingKeyAPI {

  public SettingKeyAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String name){
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.create(name);
    return settingKey.getId();
  }
  
  
  @SuppressWarnings("unused")
  private Long loggedUserId;
}
