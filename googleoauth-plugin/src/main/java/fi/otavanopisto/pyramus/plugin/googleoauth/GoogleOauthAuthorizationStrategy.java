package fi.otavanopisto.pyramus.plugin.googleoauth;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationException;
import fi.otavanopisto.pyramus.plugin.auth.ExternalAuthenticationProvider;
import fi.otavanopisto.pyramus.plugin.auth.LocalUserMissingException;
import fi.otavanopisto.pyramus.plugin.googleoauth.scribe.GoogleApi20;
import fi.otavanopisto.pyramus.util.StringUtils;

public class GoogleOauthAuthorizationStrategy implements ExternalAuthenticationProvider {
  
  private static final String GOOGLE_LOGOUT_URL = "https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=%s";
  private static final String GOOGLE_LOGGED_IN_ATTRIBUTE = "googleLoggedIn";
  private static final String STATE_ATTRIBUTE = "googleOauthState";
  
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
    String state = RandomStringUtils.secure().nextAlphanumeric(8);
    
    requestContext.getRequest().getSession().setAttribute(STATE_ATTRIBUTE, state);
    String authorizationUrl = getService().createAuthorizationUrlBuilder()
        .state(state)
        .build();
    
    requestContext.setRedirectURL(authorizationUrl);
  }

  public User processResponse(RequestContext requestContext) throws AuthenticationException {
    HttpServletRequest req = requestContext.getRequest();
    HttpSession session = req.getSession(false);

    if (session == null) {
      throw new AuthenticationException(AuthenticationException.EXTERNAL_LOGIN_SERVER_ERROR);
    }
    
    String authCode = req.getParameter("code");
    String state = req.getParameter("state");
    
    Object expectedState = session.getAttribute(STATE_ATTRIBUTE);
    session.removeAttribute(STATE_ATTRIBUTE);
    
    if (expectedState == null || !(expectedState instanceof String) || StringUtils.isBlank(state) || !StringUtils.equals(state, (String) expectedState)) {
      throw new AuthenticationException(AuthenticationException.EXTERNAL_LOGIN_SERVER_ERROR);
    }

    OAuth20Service service = getService();

    try {
      OAuth2AccessToken accessToken = service.getAccessToken(authCode);
  
      OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
      service.signRequest(accessToken, request);
      Response response = service.execute(request);
  
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
    catch (ExecutionException | IOException | InterruptedException e) {
      throw new AuthenticationException(AuthenticationException.EXTERNAL_LOGIN_SERVER_ERROR);
    }
  }

  private User processLogin(String externalId, String email) throws AuthenticationException{
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    
    // Trim the email address
    email = email != null ? email.trim() : null;
    
    Person emailPerson = personDAO.findByUniqueEmail(email);
    if (emailPerson == null) {
      throw new LocalUserMissingException(email);
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
  
  private OAuth20Service getService() {
    return new ServiceBuilder(this.getClientId())
        .apiSecret(this.getClientSecret())
        .callback(this.getRedirectUrl())
        .defaultScope(this.getScope())
        .build(new GoogleApi20());
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
