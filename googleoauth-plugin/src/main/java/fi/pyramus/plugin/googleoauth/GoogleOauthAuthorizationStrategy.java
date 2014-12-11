package fi.pyramus.plugin.googleoauth;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.system.SettingDAO;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.plugin.auth.AuthenticationException;
import fi.pyramus.plugin.auth.ExternalAuthenticationProvider;
import fi.pyramus.plugin.googleoauth.scribe.GoogleApi20;

public class GoogleOauthAuthorizationStrategy implements ExternalAuthenticationProvider {
  
  private static Logger logger = Logger.getLogger(GoogleOauthAuthorizationStrategy.class.getName());
  
  public GoogleOauthAuthorizationStrategy() {
  }

  public String getName() {
    return "Google-oauth";
  }

  public void performDiscovery(RequestContext requestContext) {
    
    OAuthService service = new ServiceBuilder()
        .provider(GoogleApi20.class)
        .apiKey(this.getClientId())
        .apiSecret(this.getClientSecret())
        .callback(this.getRedirectUrl())
        .scope(this.getScope())
        .build();

    requestContext.setRedirectURL(service.getAuthorizationUrl(null));
  }

  @SuppressWarnings("unchecked")
  public User processResponse(RequestContext requestContext) throws AuthenticationException {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    HttpServletRequest req = requestContext.getRequest();
    String authCode = req.getParameter("code");
    Verifier verifier = new Verifier(authCode);

    OAuthService service = new ServiceBuilder()
        .provider(GoogleApi20.class)
        .apiKey(this.getClientId())
        .apiSecret(this.getClientSecret())
        .callback(this.getRedirectUrl())
        .scope(this.getScope())
        .build();

    Token accessToken = service.getAccessToken(null, verifier);

    OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
    service.signRequest(accessToken, request);
    Response response = request.send();

    JSONObject userInfo = JSONObject.fromObject(response.getBody());

    if (userInfo != null) {
      User user = userDAO.findByExternalIdAndAuthProvider(userInfo.getString("id"), getName());
      if (user == null) {
        user = userDAO.findByEmail(userInfo.getString("email"));
        if (user != null && getName().equals(user.getAuthProvider())) {
            userDAO.updateExternalId(user, userInfo.getString("id"));
        } else {
          logger.log(Level.SEVERE, "Local user not found with correct authentication provider.");
          throw new AuthenticationException(AuthenticationException.LOCAL_USER_MISSING);
        }
      }
      return user;
      
    } else {
      throw new AuthenticationException(AuthenticationException.LOCAL_USER_MISSING);
    }
  }

  private String getClientId() {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO(); 
    return settingDAO.findByKey(settingKeyDAO.findByName("google.oauth.clientid")).getValue();
  }

  private String getClientSecret() {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO(); 
    return settingDAO.findByKey(settingKeyDAO.findByName("google.oauth.clientsecret")).getValue();
  }

  private String getRedirectUrl() {
    return "https://dev.pyramus.fi:8443/users/externallogin.page";
  }

  private String getScope() {
    return "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
  }
  
}
