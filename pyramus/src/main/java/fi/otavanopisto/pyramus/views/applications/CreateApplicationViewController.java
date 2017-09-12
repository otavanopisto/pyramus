package fi.otavanopisto.pyramus.views.applications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CreateApplicationViewController extends PyramusViewController {
  
  private static final Logger logger = Logger.getLogger(CreateApplicationViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    
    // Ensure attachment storage path has been properly set 
    
    try {
      SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
      SettingKey settingKey = settingKeyDAO.findByName("application.storagePath");
      if (settingKey == null) {
        logger.log(Level.SEVERE, "SettingKey for application.storagePath not found");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting == null || setting.getValue() == null) {
        logger.log(Level.SEVERE, "Setting application.storagePath not defined");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      File attachmentsFolder = Paths.get(setting.getValue()).toFile();
      if (!attachmentsFolder.exists() || !attachmentsFolder.isDirectory() || !attachmentsFolder.canWrite()) {
        logger.log(Level.SEVERE, "Setting application.storagePath refers to a non-existing or non-writable folder");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to serve 404 for application.storagePath", e);
      return;
    }
    
    // Create a unique application identifier and include the application form
    
    pageRequestContext.getRequest().setAttribute("applicationId", UUID.randomUUID().toString()); 
    pageRequestContext.setIncludeJSP("/templates/applications/application.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
