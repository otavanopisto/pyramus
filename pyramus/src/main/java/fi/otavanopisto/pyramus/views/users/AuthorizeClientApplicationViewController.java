package fi.otavanopisto.pyramus.views.users;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationErrorResponse;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.AuthorizationSuccessResponse;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.OAuth2Error;
import com.nimbusds.oauth2.sdk.ResponseMode;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.Scope.Value;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.ServletUtils;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class AuthorizeClientApplicationViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();

    HTTPRequest httpRequest;
    AuthorizationRequest authorizationRequest;
    
    try {
      httpRequest = ServletUtils.createHTTPRequest(requestContext.getRequest());
      authorizationRequest = AuthorizationRequest.parse(httpRequest);
    } catch (Exception e) {
      pageErrorResponse(requestContext);
      return;
    }

    ClientID clientID = authorizationRequest.getClientID();
    URI redirectionURI = authorizationRequest.getRedirectionURI();
    State state = authorizationRequest.getState();
    Scope requestedScopes = authorizationRequest.getScope();

    // Find the client application
    ClientApplication clientApplication = clientID != null ? clientApplicationDAO.findByClientId(clientID.getValue()) : null;
    if (clientApplication == null || !clientApplication.isActive()) {
      pageErrorResponse(requestContext);
      return;
    }

    // Check that the redirect uri is valid
    
    if (!clientApplication.isAllowAllRedirectURIs() && !clientApplication.isAllowedRedirectURI(redirectionURI)) {
      pageErrorResponse(requestContext);
      return;
    }
    
    // Check that the request is for authorization code, no other types supported at the moment
    if (!authorizationRequest.getResponseType().equals(com.nimbusds.oauth2.sdk.ResponseType.CODE)) {
      oauthErrorResponse(requestContext, OAuth2Error.UNSUPPORTED_RESPONSE_TYPE, redirectionURI, state);
      return;
    }

    // Check that there are requested scopes
    if (requestedScopes == null || requestedScopes.isEmpty()) {
      oauthErrorResponse(requestContext, OAuth2Error.INVALID_SCOPE, redirectionURI, state);
      return;
    }

    // Check that the requested scopes are valid
    Set<String> requestedScopesStrs = new HashSet<>();
    for (Value value : requestedScopes) {
      if (value == null || StringUtils.isBlank(value.getValue()) || !clientApplication.getScopes().contains(value.getValue())) {
        oauthErrorResponse(requestContext, OAuth2Error.INVALID_SCOPE, redirectionURI, state);
        return;
      }
      else {
        requestedScopesStrs.add(value.getValue());
      }
    }

    if (!requestContext.isLoggedIn()) {
      throw new LoginRequiredException(getLoginReturnUrl(requestContext), "OAUTHCLIENT", clientApplication.getClientId());
    }
    else if (clientApplication.getSkipPrompt()) {
      oauthSuccessResponse(
          requestContext, 
          clientApplication, 
          redirectionURI, 
          state != null ? state.getValue() : null, 
          requestedScopesStrs
      );
    }
    else {
      HttpServletRequest request = requestContext.getRequest();
      
      OAuthContext authContext = new OAuthContext(clientApplication.getClientId(), redirectionURI, requestedScopesStrs, state != null ? state.getValue() : null);
      request.getSession().setAttribute("pendingOAuthLoginContext", authContext);
      
      // Parameters for the consent view
      request.setAttribute("clientAppName", clientApplication.getClientName());
      request.setAttribute("authScopes", requestedScopesStrs);
      
      requestContext.setIncludeJSP("/templates/users/authorizeclientapp.jsp");
    }
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    if (!requestContext.isLoggedIn()) {
      throw new LoginRequiredException(getLoginReturnUrl(requestContext));
    }
    
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();

    HttpServletRequest request = requestContext.getRequest();
    HttpSession session = request.getSession(false);
    OAuthContext authContext = session != null ? (OAuthContext) session.getAttribute("pendingOAuthLoginContext") : null;

    if (authContext != null) {
      if (StringUtils.isNotBlank(request.getParameter("authorize"))) {
        ClientApplication clientApplication = clientApplicationDAO.findByClientId(authContext.getClientId());
        
        oauthSuccessResponse(requestContext, clientApplication, authContext);
        return;
      }
      else if (StringUtils.isNotBlank(request.getParameter("deny"))) {
        session.removeAttribute("pendingOAuthLoginContext");
        
        State stateObj = State.parse(authContext.getState());
        oauthErrorResponse(requestContext, OAuth2Error.ACCESS_DENIED, authContext.getRedirectURI(), stateObj);
        return;
      }
    }

    // Fallback if there's issues with the form or the session
    pageErrorResponse(requestContext);
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  private boolean oauthSuccessResponse(PageRequestContext requestContext, ClientApplication clientApplication, OAuthContext authContext) {
    return oauthSuccessResponse(requestContext, clientApplication, authContext.getRedirectURI(), authContext.getState(), authContext.getScopes());
  }
  
  private boolean oauthSuccessResponse(PageRequestContext requestContext, ClientApplication clientApplication, URI redirectURI, String state, Set<String> requestedScopesStrs) {
    ClientApplicationAuthorizationCodeDAO clientApplicationAuthorizationCodeDAO = DAOFactory.getInstance().getClientApplicationAuthorizationCodeDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    // TODO doublecheck the scopes and redirecturi ?
    
    HttpSession session = requestContext.getRequest().getSession();
    Long userId = (Long) session.getAttribute("loggedUserId");
    User user = userDAO.findById(userId);

    AuthorizationCode authorizationCode = new AuthorizationCode();
    AccessToken accessToken = null; // Not applicable here
    State stateObj = State.parse(state);
    Issuer issuer = null;
    ResponseMode responseMode = null;

    clientApplicationAuthorizationCodeDAO.create(user, clientApplication, authorizationCode.getValue(), redirectURI.toString(), requestedScopesStrs);
    
    AuthorizationSuccessResponse authorizationSuccessResponse = new AuthorizationSuccessResponse(redirectURI, authorizationCode, accessToken, stateObj, issuer, responseMode);
    
    requestContext.setRedirectURL(authorizationSuccessResponse.toURI().toString());
    return true;
  }

  private void oauthErrorResponse(PageRequestContext requestContext, ErrorObject reason, URI redirectURI, State state) {
    Issuer issuer = null;
    ResponseMode responseMode = null;
    
    AuthorizationErrorResponse authorizationErrorResponse = new AuthorizationErrorResponse(redirectURI, reason, state, issuer, responseMode);
    requestContext.setRedirectURL(authorizationErrorResponse.toURI().toString());
  }
  
  private String getLoginReturnUrl(PageRequestContext requestContext) {
    HttpServletRequest request = requestContext.getRequest();
    StringBuilder currentUrl = new StringBuilder(request.getRequestURL());
    String queryString = request.getQueryString();
    if (!StringUtils.isBlank(queryString)) {
      currentUrl.append('?');
      currentUrl.append(queryString);
    }
    return currentUrl.toString();
  }
  
  private void pageErrorResponse(PageRequestContext requestContext) {
    requestContext.getRequest().setAttribute("loginStatus", "ERROR");
    requestContext.setIncludeJSP("/templates/users/authorizeclientapp.jsp");
  }

  public class OAuthContext {
    public OAuthContext(String clientId, URI redirectURI, Set<String> scopes, String state) {
      this.clientId = clientId;
      this.redirectURI = redirectURI;
      this.scopes = scopes;
      this.state = state;
    }
    
    public String getClientId() {
      return clientId;
    }

    public URI getRedirectURI() {
      return redirectURI;
    }
    
    public Set<String> getScopes() {
      return scopes;
    }
    
    public String getState() {
      return state;
    }
    
    private final String clientId;
    private final URI redirectURI;
    private final Set<String> scopes;
    private final String state;
  }
}
