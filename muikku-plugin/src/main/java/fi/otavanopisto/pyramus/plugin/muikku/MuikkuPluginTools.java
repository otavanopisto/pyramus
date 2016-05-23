package fi.otavanopisto.pyramus.plugin.muikku;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;

public class MuikkuPluginTools {

  public static String getMuikkuHost() {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKey key = settingKeyDAO.findByName("muikkuplugin.muikkuhost");
    if (key != null) {
      Setting setting = settingDAO.findByKey(key);
      if (setting != null) {
        return setting.getValue();
      }
    }
    
    return null;
  }
  
}
