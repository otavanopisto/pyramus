package fi.pyramus.plugin.googleoauth;

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
import fi.pyramus.dao.base.PersonDAO;
import fi.pyramus.dao.system.SettingDAO;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.dao.users.UserIdentificationDAO;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.UserIdentification;
import fi.pyramus.plugin.auth.AuthenticationException;
import fi.pyramus.plugin.auth.ExternalAuthenticationProvider;
import fi.pyramus.plugin.googleoauth.scribe.GoogleApi20;

public class GoogleOauthAuthorizationStrategy implements ExternalAuthenticationProvider {
  
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

  public User processResponse(RequestContext requestContext) throws AuthenticationException {
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
      return processLogin(userInfo.getString("id"), userInfo.getString("email"));
    } else {
      throw new AuthenticationException(AuthenticationException.EXTERNAL_LOGIN_SERVER_ERROR);
    }
  }

  private User processLogin(String externalId, String email) throws AuthenticationException{
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    
    Person emailPerson = personDAO.findByUniqueEmail(email);
    if(emailPerson == null){
      throw new AuthenticationException(AuthenticationException.LOCAL_USER_MISSING);
    }
    UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndExternalId(getName(), externalId);
    if (userIdentification != null) {
      // User has identified by this auth source before
      if (!emailPerson.getId().equals(userIdentification.getPerson().getId())) {
        throw new AuthenticationException(AuthenticationException.EMAIL_BELONGS_TO_ANOTHER_PERSON);
      }
    }else{
      userIdentificationDAO.create(emailPerson, getName(), externalId);
    }
    return emailPerson.getDefaultUser();
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
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO(); 
    return settingDAO.findByKey(settingKeyDAO.findByName("google.oauth.redirecturl")).getValue();
  }

  private String getScope() {
    return "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
  }
  
}
