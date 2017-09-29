package fi.otavanopisto.pyramus.views.applications;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ManageApplicationViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(EditApplicationViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    try {
      
      Long applicationId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("application"));
      if (applicationId == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationId);
      if (application == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      
      // Hakemuksen tilatiedot

      pageRequestContext.getRequest().setAttribute("infoState", ApplicationUtils.applicationStateUiValue(application.getState()));
      pageRequestContext.getRequest().setAttribute("infoApplicantEditable", application.getApplicantEditable());
      if (application.getHandler() != null) {
        pageRequestContext.getRequest().setAttribute("infoHandler", application.getHandler().getFullName());
      }
      pageRequestContext.getRequest().setAttribute("infoLastModified", ApplicationUtils.getLatest(
          application.getLastModified(),
          application.getApplicantLastModified(),
          application.getCreated()));
      
      pageRequestContext.getRequest().setAttribute("applicationEntityId", application.getId());      
      pageRequestContext.getRequest().setAttribute("applicationId", application.getApplicationId());
      
      // Editointinäkymä

      pageRequestContext.getRequest().setAttribute("mode", "edit");
      pageRequestContext.getRequest().setAttribute("referenceCode", application.getReferenceCode());
      pageRequestContext.getRequest().setAttribute("preload", Boolean.TRUE);
      pageRequestContext.getRequest().setAttribute("donePage", Boolean.FALSE);
      pageRequestContext.getRequest().setAttribute("saveUrl", "/applications/saveapplication.json");
      
      pageRequestContext.setIncludeJSP("/templates/applications/management-edit-application.jsp");
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to serve error response", e);
      return;
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
