package fi.otavanopisto.pyramus.rest.session;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.rest.controller.OauthController;
import fi.otavanopisto.pyramus.security.impl.AbstractSessionControllerImpl;
import fi.otavanopisto.pyramus.security.impl.Permissions;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.security.ContextReference;

@RestSession
@RequestScoped
public class RestSessionControllerImpl extends AbstractSessionControllerImpl implements SessionController {
  
  @Inject
  private Permissions permissions;
  
  @Inject
  private HttpServletRequest request;

  @Inject
  private OauthController oauthController;

  @Inject
  private UserDAO userDAO;
  
  @Override
  public void logout() {
  }
  
  @Override
  public Locale getLocale() {
    return locale;
  }
  
  @Override
  public void setLocale(Locale locale) {
    this.locale = locale;
  }
  
  @PostConstruct
  private void init() {
  }
  
  @Override
  public boolean hasPermission(String permissionName, ContextReference contextReference) {
    return permissions.hasPermission(getUser(), permissionName, contextReference);
  }
  
  @Override
  public User getUser() {
    User user = getOAuthUser();
    if (user == null) {
      user = getSessionUser();
    }

    return user;
  }
  
  private User getSessionUser() {
    HttpSession session = request.getSession(false);
    if (session != null) {
      Long loggedUserId = (Long) session.getAttribute("loggedUserId");
      if (loggedUserId != null) {
        return userDAO.findById(loggedUserId);
      }
    }
    
    return null;
  }

  private User getOAuthUser() {
    try {
      OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
      String accessToken = oauthRequest.getAccessToken();

      ClientApplicationAccessToken clientApplicationAccessToken = oauthController.findByAccessToken(accessToken);
      if (clientApplicationAccessToken != null) {
        Long currentTime = System.currentTimeMillis() / 1000L;
        if (currentTime <= clientApplicationAccessToken.getExpires()) {
//          System.out.println("RESTSession.getUser: " +
//              " UserId=" + clientApplicationAccessToken.getClientApplicationAuthorizationCode().getUser().getId().toString() +
//              " role=" + clientApplicationAccessToken.getClientApplicationAuthorizationCode().getUser().getRole().toString() +
//              " accessToken=" + accessToken);

          return clientApplicationAccessToken.getClientApplicationAuthorizationCode().getUser();
        }
      }
    } catch (OAuthProblemException | OAuthSystemException e) {
      return null;
    }

    return null;
  }
  
  @Override
  public boolean hasEnvironmentPermission(String permission) {
    return hasPermission(permission, null);
  }
  
  @Override
  public boolean isLoggedIn() {
    return getUser() != null;
  }
  
  private Locale locale;

  
//  @Inject
//  private CourseEntityDAO courseDAO;
  
//  @Override
//  public void setAuthentication(RestAuthentication authentication) {
//    this.authentication = authentication;
//  }
//
//  @Override
//  public Locale getLocale() {
//    return locale;
//  }
//  
//  @Override
//  public void setLocale(Locale locale) {
//    this.locale = locale;
//  }
//  
//  @Override
//  public User getUser() {
//    if (authentication != null)
//      return authentication.getUser();
//    
//    return null;
//  }
//
//  @Override
//  public boolean isLoggedIn() {
//    if (authentication != null)
//      return authentication.isLoggedIn();
//    
//    return false;
//  }
//  
//  public void logout() {
//    if (authentication != null)
//      authentication.logout();
//  }
//  
//  @Override
//  protected boolean hasEnvironmentPermissionImpl(String permission) {
//    return hasPermission(permission, null);
//  }
//
//  @Override
//  protected boolean hasCoursePermissionImpl(String permission, WorkspaceEntity course) {
//    return hasPermissionImpl(permission, course);
//  }
//  
//  @Override
//  protected boolean hasResourcePermissionImpl(String permission, ResourceEntity resource) {
//    return hasPermissionImpl(permission, resource);
//  }
//
//  @Override
//  protected boolean hasPermissionImpl(String permission, ContextReference contextReference) {
//    PermissionResolver permissionResolver = getPermissionResolver(permission);
//
//    if (isLoggedIn()) {
//      return isSuperuser() || permissionResolver.hasPermission(permission, contextReference, getUser());
//    } else {
//      return permissionResolver.hasEveryonePermission(permission, contextReference);
//    }
//  }
//
//  @Override
//  public void addOAuthAccessToken(String strategy, Date expires, String accessToken) {
//    accessTokens.put(strategy, new AccessToken(accessToken, expires));
//  }
//
//  @Override
//  public AccessToken getOAuthAccessToken(String strategy) {
//    return accessTokens.get(strategy);
//  }
//
//  private RestAuthentication authentication;
//  private Locale locale;
//
//  private Map<String, AccessToken> accessTokens = Collections.synchronizedMap(new HashMap<String, AccessToken>());
}
