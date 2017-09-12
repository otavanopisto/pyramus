package fi.otavanopisto.pyramus.views.applications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditApplicationViewController extends PyramusViewController {
  
  private static final Logger logger = Logger.getLogger(EditApplicationViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    
    String applicationId = pageRequestContext.getRequest().getParameter("applicationId");
    String referer = pageRequestContext.getRequest().getHeader("Referer");
    String requestUrl = pageRequestContext.getRequest().getRequestURL().toString();
    try {
      
      // Ensure applicationId has been provided by this page
      
      if (applicationId != null && !StringUtils.equals(referer, requestUrl)) {
        logger.warning(String.format("Refused application edit. Application id %s from referer %s", applicationId, referer));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
      }

      // Ensure attachment storage path has been properly set 
      
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
      
      // If no applicationId has been set, include gateway page
      
      if (applicationId == null) {
        pageRequestContext.setIncludeJSP("/templates/applications/edit.jsp");
      }
      else {
        
        // Find application by applicationId and pass form data to page
        
        ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
        Application application = applicationDAO.findByApplicationId(applicationId);
        if (application == null || application.getArchived()) {
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        // TODO Evaluate application state; can the data still be modified by applicant?
        pageRequestContext.getRequest().setAttribute("applicationId", applicationId);
        pageRequestContext.getRequest().setAttribute("referenceCode", application.getReferenceCode());
        pageRequestContext.getRequest().setAttribute("preload", Boolean.TRUE);
        pageRequestContext.getRequest().setAttribute("donePage", Boolean.TRUE);
        pageRequestContext.getRequest().setAttribute("saveUrl", "/1/applications/saveapplication");
        pageRequestContext.setIncludeJSP("/templates/applications/application.jsp");
      }
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to serve error response", e);
      return;
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
