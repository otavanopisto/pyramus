package fi.pyramus.views.users;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.plugin.auth.AuthenticationException;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.ExternalAuthenticationProvider;
import fi.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.pyramus.plugin.auth.LocalUserMissingException;

public class ExternalLoginLoginViewController extends PyramusViewController {

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  // TODO: Does not support multiple external strategies
  public void process(PageRequestContext requestContext) {
    // Ensure that the user trying to login isn't already logged in
    
    Locale locale = requestContext.getRequest().getLocale();
    HttpSession session = requestContext.getRequest().getSession(true);
    if (!session.isNew() && session.getAttribute("loggedUserId") != null) {
      String msg = Messages.getInstance().getText(locale, "users.login.alreadyLoggedIn");
      throw new SmvcRuntimeException(PyramusStatusCode.ALREADY_LOGGED_IN, msg);
    }

    AuthenticationProviderVault authenticationProviders = AuthenticationProviderVault.getInstance();
    
    try {
      ExternalAuthenticationProvider authenticationProvider = authenticationProviders.getExternalAuthenticationProviders().get(0);
      User user = authenticationProvider.processResponse(requestContext);
      if (user != null) { 
        // User has been authorized, so store him in the session
        
        session.setAttribute("loggedUserId", user.getId());
        session.setAttribute("loggedUserName", user.getFullName());
        session.setAttribute("authenticationProvider", authenticationProvider.getName());

        if (user instanceof StaffMember) {
          session.setAttribute("loggedUserRole", UserRole.valueOf(((StaffMember) user).getRole().name()));
        }
        
        // If the session contains a followup URL, redirect there and if not, redirect to the index page 
        
        if (session.getAttribute("loginRedirectUrl") != null) {
          String url = (String) session.getAttribute("loginRedirectUrl");
          session.removeAttribute("loginRedirectUrl");
          requestContext.setRedirectURL(url);
        }
        else {
          requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/index.page");
        }
      } else {
        String msg = Messages.getInstance().getText(requestContext.getRequest().getLocale(), "users.login.loginFailed");
        throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, msg);
      }
    } catch (LocalUserMissingException lume) {
      List<InternalAuthenticationProvider> internalAuthenticationProviders = authenticationProviders.getInternalAuthenticationProviders();
      List<ExternalAuthenticationProvider> externalAuthenticationProviders = authenticationProviders.getExternalAuthenticationProviders();
      
      if (!internalAuthenticationProviders.isEmpty() || externalAuthenticationProviders.size() > 1) {
        try {
          requestContext.setRedirectURL(String.format("%s/users/login.page?localUserMissing=%s", requestContext.getRequest().getContextPath(), URLEncoder.encode(lume.getExternalUser(), "UTF-8")));
        } catch (UnsupportedEncodingException e) {
          throw new SmvcRuntimeException(e);
        }
      } else {
        throw new SmvcRuntimeException(PyramusStatusCode.LOCAL_USER_MISSING, Messages.getInstance().getText(locale, "users.login.localUserMissing", new String[] { lume.getExternalUser()  }));
      }
    } catch (AuthenticationException ae) {
      throw new SmvcRuntimeException(ae);
    }
  }

}
