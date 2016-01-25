package fi.pyramus.plugin.googleoauth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
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
  
  private static final String GOOGLE_LOGOUT_URL = "https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=%s";
  private static final String GOOGLE_LOGGED_IN_ATTRIBUTE = "googleLoggedIn";
  
  public GoogleOauthAuthorizationStrategy() {
  }

  public String getName() {
    return "Google-oauth";
  }
  
  @Override
  public String logout(RequestContext requestContext) {
    HttpSession session = requestContext.getRequest().getSession(false);
    if (!isLoggedInToGoogle(session)) {
      return null;
    } else {
      String logoutUrl = String.format("%s/users/logout.page", getBaseUrl(requestContext));
      
      String redirectUrl = requestContext.getString("redirectUrl");
      if (StringUtils.isNotBlank(redirectUrl)) {
        logoutUrl = String.format("%s?redirectUrl=%s", logoutUrl, redirectUrl);
      }
      
      try {
        return String.format(GOOGLE_LOGOUT_URL, logoutUrl);
      } finally {
        setGoogleLoggedIn(session, false);
      }
    }
  }

  private boolean isLoggedInToGoogle(HttpSession session) {
    return Boolean.TRUE.equals(session.getAttribute(GOOGLE_LOGGED_IN_ATTRIBUTE));
  }
  
  private void setGoogleLoggedIn(HttpSession session, Boolean value) {
    session.setAttribute(GOOGLE_LOGGED_IN_ATTRIBUTE, value);
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
    HttpSession session = req.getSession();
    
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
      try {
        return processLogin(userInfo.getString("id"), userInfo.getString("email"));
      } finally {
        setGoogleLoggedIn(session, true);
      }
    } else {
      throw new AuthenticationException(AuthenticationException.EXTERNAL_LOGIN_SERVER_ERROR);
    }
  }

  private User processLogin(String externalId, String email) throws AuthenticationException{
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    
    // Trim the email address
    email = email != null ? email.trim() : null;
    
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
    } else {
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
  
  private String getBaseUrl(RequestContext requestContext) {
    HttpServletRequest request = requestContext.getRequest();
    String currentURL = request.getRequestURL().toString();
    String pathInfo = request.getRequestURI();
    return currentURL.substring(0, currentURL.length() - pathInfo.length()) + request.getContextPath();
  }
  
}
