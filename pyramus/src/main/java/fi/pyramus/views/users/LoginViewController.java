package fi.pyramus.views.users;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.ExternalAuthenticationProvider;

/**
 * The controller responsible of the Login view of the application. 
 * 
 * @see fi.pyramus.json.users.LoginJSONRequestController
 */
public class LoginViewController extends PyramusViewController {

  /**
   * Processes the page request. This is a simple case of just including the corresponding login JSP page.
   * Since the login form is submitted via JSON, the actual logic of logging in takes place in
   * {@link fi.pyramus.json.users.LoginJSONRequestController}. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext requestContext) {
    AuthenticationProviderVault authenticationProviders = AuthenticationProviderVault.getInstance();
    boolean hasInternals = authenticationProviders.hasInternalStrategies();
    boolean hasExternals = authenticationProviders.hasExternalStrategies();
    
    if (hasExternals && hasInternals) {
      requestContext.setIncludeJSP("/templates/users/login_both.jsp");
    } else {
      if (hasExternals) {
        // TODO: Does not support multiple external providers, yet
        ExternalAuthenticationProvider authenticationProvider = authenticationProviders.getExternalAuthenticationProviders().get(0);
        authenticationProvider.performDiscovery(requestContext);
      } else { 
        requestContext.setIncludeJSP("/templates/users/login_internal.jsp");
      }
    }
  }

  /**
   * Returns the roles allowed to access this page. Naturally, logging in is available for {@link Role#EVERYONE}.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
