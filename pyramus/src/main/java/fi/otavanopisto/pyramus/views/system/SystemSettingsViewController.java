package fi.otavanopisto.pyramus.views.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of the system settings view of the application.
 */
public class SystemSettingsViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    
    Map<String, String> settings = new HashMap<>();
    List<SettingKey> settingKeys = settingKeyDAO.listAll();
    for (SettingKey settingKey : settingKeys) {
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null)
        settings.put(settingKey.getName(), setting.getValue());
    }
    
    requestContext.getRequest().setAttribute("settingKeys", settingKeys);
    requestContext.getRequest().setAttribute("settings", settings);
    
    requestContext.setIncludeJSP("/templates/system/systemsettings.jsp");
  }
  
  @Override
  public void processSend(PageRequestContext requestContext) {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    
    Long rowCount = requestContext.getLong("settingsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "settingsTable." + i;
      
      String key = requestContext.getString(colPrefix + ".key");
      String value = requestContext.getString(colPrefix + ".value");
      boolean hasValue = !StringUtils.isBlank(value);
      
      SettingKey settingKey = settingKeyDAO.findByName(key);
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null) {
        if (!hasValue)
          settingDAO.delete(setting);
        else
          settingDAO.update(setting, settingKey, value);
      } else {
        if (hasValue)
          settingDAO.create(settingKey, value);
      }
    }
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/system/systemsettings.page");
  }
  
 
  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
