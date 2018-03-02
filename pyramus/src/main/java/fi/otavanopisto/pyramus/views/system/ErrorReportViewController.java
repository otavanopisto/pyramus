package fi.otavanopisto.pyramus.views.system;

import java.util.logging.Level;
import java.util.logging.Logger;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of the system settings view of the application.
 */
public class ErrorReportViewController extends PyramusFormViewController {

  private static final Logger logger = Logger.getLogger(ErrorReportViewController.class.getName());
  
  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/system/errorreport.jsp");
  }
  
  @Override
  public void processSend(PageRequestContext requestContext) {
    String logEntry = requestContext.getString("errorMessage").replace("\n", " ").replace("\r", " ");
    logger.log(Level.INFO, logEntry);
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/system/errorreport.page");
  }
  
 
  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] {UserRole.EVERYONE};
  }

}
