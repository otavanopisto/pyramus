package fi.otavanopisto.pyramus.json.users;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;

import fi.internetix.smvc.AlreadyLoggedInException;
import fi.internetix.smvc.InvalidLoginException;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationException;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.plugin.auth.LocalUserMissingException;

/**
 * The controller responsible of logging in the user with the credentials he has provided. 
 * 
 * @see fi.otavanopisto.pyramus.views.users.LoginViewController
 */
public class LoginJSONRequestController extends JSONRequestController {
  
  /**
   * Processes the request to log in. Authorizes the given credentials and if they match a user,
   * stores the user into the session (keys <code>loggedUserId</code>, <code>loggedUserName</code>,
   * and <code>loggedUserRoles</code>).
   * <p/>
   * If the session contains a <code>loginRedirectUrl</code> key, redirects the user to that URL.
   * Otherwise, redirects back to the index page of the application.
   * <p/>
   * If the user is already logged in or the authentication fails, a <code>PyramusRuntimeException</code>
   * is thrown with a localized message stating so.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    
    // Fields submitted from the web page
    
    String username = jsonRequestContext.getRequest().getParameter("username");
    String password = jsonRequestContext.getRequest().getParameter("password");
    Locale locale = jsonRequestContext.getRequest().getLocale();
    
    // Ensure that the user trying to login isn't already logged in
    
    HttpSession session = jsonRequestContext.getRequest().getSession(true);
    if (!session.isNew() && session.getAttribute("loggedUserId") != null) {
      String msg = Messages.getInstance().getText(locale, "users.login.alreadyLoggedIn");
      throw new AlreadyLoggedInException(PyramusStatusCode.ALREADY_LOGGED_IN, msg);
    }
    
    // Go through all authentication providers and see if one authorizes the given credentials
    
    for (InternalAuthenticationProvider provider : AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders()) {
      try {
        User user = provider.getUser(username, password);
        if (user != null && !user.getArchived() && user.isAccountEnabled()) {
          
          // User has been authorized, so store him in the session
          
          session.setAttribute("loggedUserId", user.getId());
          session.setAttribute("loggedUserName", user.getFullName());
          session.setAttribute("authenticationProvider", provider.getName());
          if (user instanceof StaffMember) {
            session.setAttribute("loggedUserRoles", Set.copyOf(((StaffMember) user).getRoles()));
          }

          try {
            DAOFactory.getInstance().getLoginLogDAO().create(user, new Date());
          } catch (Exception ex) {
            ex.printStackTrace();
          }
          
          // If the session contains a followup URL, redirect there and if not, redirect to the index page 
          
          if (session.getAttribute("loginRedirectUrl") != null) {
            String url = (String) session.getAttribute("loginRedirectUrl");
            session.removeAttribute("loginRedirectUrl");
            jsonRequestContext.setRedirectURL(url);
          }
          else {
            jsonRequestContext.setRedirectURL(jsonRequestContext.getRequest().getContextPath() + "/index.page");
          }
          return;
        }
      } catch (LocalUserMissingException lume) {
        throw new SmvcRuntimeException(PyramusStatusCode.LOCAL_USER_MISSING, Messages.getInstance().getText(locale, "users.login.localUserMissing", new String[] { lume.getExternalUser()  }));
      } catch (AuthenticationException ae) {
        throw new SmvcRuntimeException(ae);
      }
    }
    
    // Reaching this point means no authentication provider authorized the user, so throw a login exception 

    String msg = Messages.getInstance().getText(jsonRequestContext.getRequest().getLocale(), "users.login.loginFailed");
    throw new InvalidLoginException(msg);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
