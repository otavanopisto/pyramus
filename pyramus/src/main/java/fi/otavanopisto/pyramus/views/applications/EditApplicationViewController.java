package fi.otavanopisto.pyramus.views.applications;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditApplicationViewController extends PyramusViewController {
  
  private static final Logger logger = Logger.getLogger(EditApplicationViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    String applicationId = pageRequestContext.getRequest().getParameter("applicationId");
    try {
      
      // Ensure applicationId has been provided by this page
      
      if (applicationId != null) {
        boolean validEntry = false;
        String referer = pageRequestContext.getRequest().getHeader("Referer");
        if (referer != null) {
          try {
            URI requestUri = new URI(pageRequestContext.getRequest().getRequestURL().toString());
            URI refererUri = new URI(pageRequestContext.getRequest().getHeader("Referer"));
            validEntry = StringUtils.equals(requestUri.getHost(), refererUri.getHost()) &&
                StringUtils.equals(requestUri.getPath(), refererUri.getPath());
          }
          catch (URISyntaxException e) {
          }
        }
        if (!validEntry) {
          logger.warning(String.format("Refused application edit. Application id %s from referer %s", applicationId, referer));
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
      }

      // Ensure attachment storage path has been properly set 
      
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
      
      // If no applicationId has been set, include gateway page
      
      if (applicationId == null) {
        pageRequestContext.setIncludeJSP("/templates/applications/application-edit-gateway.jsp");
      }
      else {
        
        // Find application by applicationId and pass form data to page
        
        ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
        Application application = applicationDAO.findByApplicationId(applicationId);
        if (application == null || application.getArchived()) {
          pageRequestContext.getRequest().setAttribute("notFound", Boolean.TRUE);
          pageRequestContext.setIncludeJSP("/templates/applications/application-edit-gateway.jsp");
        }
        else if (Boolean.FALSE.equals(application.getApplicantEditable())) {
          pageRequestContext.getRequest().setAttribute("locked", Boolean.TRUE);
          if (application.getHandler() != null) {
            pageRequestContext.getRequest().setAttribute("handlerName", application.getHandler().getFullName());
            Email email = application.getHandler().getPrimaryEmail();
            if (email != null && email.getAddress() != null) {
              pageRequestContext.getRequest().setAttribute("handlerEmail", email.getAddress());
            }
          }
          pageRequestContext.setIncludeJSP("/templates/applications/application-edit-gateway.jsp");
        }
        else {
          pageRequestContext.getRequest().setAttribute("applicationId", applicationId);
          pageRequestContext.getRequest().setAttribute("referenceCode", application.getReferenceCode());
          pageRequestContext.getRequest().setAttribute("preload", Boolean.TRUE);
          pageRequestContext.getRequest().setAttribute("donePage", Boolean.TRUE);
          pageRequestContext.getRequest().setAttribute("saveUrl", "/1/applications/saveapplication");
          pageRequestContext.setIncludeJSP("/templates/applications/application-edit.jsp");
        }
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
