package fi.otavanopisto.pyramus.views.users;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.PageNotFoundException;
import fi.internetix.smvc.Severity;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.plugin.PluginManager;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProvider;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.ExternalAuthenticationProvider;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

/**
 * The controller responsible of the Login view of the application. 
 * 
 * @see fi.otavanopisto.pyramus.json.users.LoginJSONRequestController
 */
public class LoginViewController extends PyramusViewController {

  /**
   * Processes the page request. This is a simple case of just including the corresponding login JSP page.
   * Since the login form is submitted via JSON, the actual logic of logging in takes place in
   * {@link fi.otavanopisto.pyramus.json.users.LoginJSONRequestController}. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext requestContext) {
    AuthenticationProviderVault authenticationProviders = AuthenticationProviderVault.getInstance();
    boolean hasInternals = authenticationProviders.hasInternalStrategies();
    boolean hasExternals = authenticationProviders.hasExternalStrategies();
    List<InternalAuthenticationProvider> internalAuthenticationProviders = authenticationProviders.getInternalAuthenticationProviders();
    List<ExternalAuthenticationProvider> externalAuthenticationProviders = authenticationProviders.getExternalAuthenticationProviders();
    
    String external = requestContext.getString("external");
    if (StringUtils.isNotBlank(external)) {
      AuthenticationProvider authenticationProvider = authenticationProviders.getAuthenticationProvider(external);
      if (authenticationProvider instanceof ExternalAuthenticationProvider) {
        ((ExternalAuthenticationProvider) authenticationProvider).performDiscovery(requestContext);
      } else {
        throw new PageNotFoundException(requestContext.getRequest().getLocale());
      }
    } else {
      if (!hasInternals && hasExternals && externalAuthenticationProviders.size() == 1) {
        ExternalAuthenticationProvider authenticationProvider = authenticationProviders.getExternalAuthenticationProviders().get(0);
        authenticationProvider.performDiscovery(requestContext);
      } else {
        // TODO: support for multiple internal providers 
        requestContext.getRequest().setAttribute("internalProviders", internalAuthenticationProviders);
        requestContext.getRequest().setAttribute("externalProviders", externalAuthenticationProviders);
        String localUserMissing = requestContext.getString("localUserMissing");
        if (StringUtils.isNotBlank(localUserMissing)) {
          requestContext.addMessage(Severity.WARNING, Messages.getInstance().getText(requestContext.getRequest().getLocale(), 
              "users.login.localUserMissing", new String[] { localUserMissing }));
        }
        
        String customLoginPage = getCustomLoginPage(requestContext);
        if (StringUtils.isNotBlank(customLoginPage)) {
          requestContext.setIncludeFtl(customLoginPage);
        } else {
          requestContext.setIncludeJSP("/templates/users/login.jsp");
        }
      }
    }
  }

  private String getCustomLoginPage(PageRequestContext requestContext) {
    String loginContextType = getLoginContextType(requestContext);
    String loginContextId = getLoginContextId(requestContext);
    
    if (StringUtils.isNotBlank(loginContextType) && StringUtils.isNotBlank(loginContextId)) {
      return PluginManager.getInstance().getCustomLoginScreen(loginContextType, loginContextId);
    }
    
    return null;
  }

  /**
   * Returns the roles allowed to access this page. Naturally, logging in is available for {@link Role#EVERYONE}.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  private String getLoginContextType(RequestContext requestContext) {
    HttpSession session = requestContext.getRequest().getSession();
    String contextType = (String) session.getAttribute("loginContextType");
    return StringUtils.isBlank(contextType) ? "INTERNAL" : contextType;
  }

  private String getLoginContextId(RequestContext requestContext) {
    HttpSession session = requestContext.getRequest().getSession();
    String contextId = (String) session.getAttribute("loginContextId");
    return StringUtils.isBlank(contextId) ? null : contextId;
  }
  
}
