package fi.pyramus.views.users;

import javax.servlet.http.HttpSession;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of logging the user out. 
 */
public class LogoutViewController extends PyramusViewController {
  
  /**
   * Processes the page request. Simply invalidates the session of the logged in user
   * and redirects back to the index page of the application. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext requestContext) {
    if (requestContext.getLoggedUserId() != null) {
      HttpSession session = requestContext.getRequest().getSession(false);
      session.invalidate();
    }
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/index.page");
  }
  
  /**
   * Returns the roles allowed to access this page. Logging out is available
   * for all logged in users.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
