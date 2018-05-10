package fi.otavanopisto.pyramus.views.applications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
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
      SettingKey settingKey = settingKeyDAO.findByName("applications.storagePath");
      if (settingKey == null) {
        logger.log(Level.SEVERE, "SettingKey for applications.storagePath not found");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting == null || setting.getValue() == null) {
        logger.log(Level.SEVERE, "Setting applications.storagePath not defined");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      File attachmentsFolder = Paths.get(setting.getValue()).toFile();
      if (!attachmentsFolder.exists() || !attachmentsFolder.isDirectory() || !attachmentsFolder.canWrite()) {
        logger.log(Level.SEVERE, "Setting applications.storagePath refers to a non-existing or non-writable folder");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to serve 404 for applications.storagePath", e);
      return;
    }
    
    String line = pageRequestContext.getString("line");
    if (!StringUtils.isBlank(line) && ApplicationUtils.isValidLine(line)) {
      pageRequestContext.getRequest().setAttribute("preselectLine", line);
      pageRequestContext.getRequest().setAttribute("applicationId", UUID.randomUUID().toString()); 
      pageRequestContext.getRequest().setAttribute("preload", Boolean.FALSE);
      pageRequestContext.getRequest().setAttribute("donePage", Boolean.TRUE);
      pageRequestContext.getRequest().setAttribute("saveUrl", "/1/applications/saveapplication");
      pageRequestContext.setIncludeJSP("/templates/applications/application-edit.jsp");
    }
    else {
      pageRequestContext.setRedirectURL(pageRequestContext.getRequest().getContextPath() + "/applications/index.page");
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
