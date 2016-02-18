package fi.pyramus.views.users;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.plugin.auth.AuthenticationProvider;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;

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
    HttpSession session = requestContext.getRequest().getSession(true);
    String redirectUrl = null;
    
    String authenticationProviderName = (String) session.getAttribute("authenticationProvider");
    if (StringUtils.isNotBlank(authenticationProviderName)) {
      AuthenticationProvider authenticationProvider = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authenticationProviderName);
      if (authenticationProvider == null) {
        Logger.getLogger(getClass().getName()).severe(String.format("Could not find authenticationProvider %s", authenticationProviderName));
      } else {
        redirectUrl = authenticationProvider.logout(requestContext);
      }
    }

    if (StringUtils.isNotBlank(redirectUrl)) {
      requestContext.setRedirectURL(redirectUrl);
    } else {
      if (requestContext.getLoggedUserId() != null) {
        session.invalidate();
      }
      
      redirectUrl = requestContext.getString("redirectUrl");
      
      if (StringUtils.isBlank(redirectUrl)) {
        redirectUrl = requestContext.getRequest().getContextPath() + "/index.page";
      }
      
      requestContext.setRedirectURL(redirectUrl);
    }
  }
  
  /**
   * Returns the roles allowed to access this page. Logging out is available
   * for all logged in users.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }
  
}
