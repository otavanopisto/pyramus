package fi.otavanopisto.pyramus.rest.controller;

import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;

import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAccessTokenDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class OauthController {

  @Inject
  private ClientApplicationDAO clientApplicationDAO;

  @Inject
  private ClientApplicationAuthorizationCodeDAO clientApplicationAuthorizationCodeDAO;
  
  @Inject
  private ClientApplicationAccessTokenDAO clientApplicationAccessTokenDAO;

  /**
   * Attempts to parse the Bearer Authorization header from the request and
   * return the ClientApplicationAccessToken that matches the access token.
   * 
   * Returns null if the parsing fails or if there is no access token that
   * matches the header.
   * 
   * @param httpRequest
   * @return
   */
  public ClientApplicationAccessToken getAccessTokenFromRequest(HttpServletRequest httpRequest) {
    try {
      BearerAccessToken bearerAccessToken = BearerAccessToken.parse(httpRequest.getHeader(HttpHeaders.AUTHORIZATION));
      String accessToken = bearerAccessToken != null ? bearerAccessToken.getValue() : null;
      return StringUtils.isNotBlank(accessToken) ? findByAccessToken(accessToken) : null;
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Creates new access token based on the authorization code 
   * and removes the authorization code.
   * 
   * @param clientApplicationAuthorizationCode
   * @param accessToken
   * @param refreshToken
   * @param expires
   * @param scopes
   * @return
   */
  public ClientApplicationAccessToken tradeAuthorizationCodeForAccessToken(ClientApplicationAuthorizationCode clientApplicationAuthorizationCode, String accessToken, String refreshToken, Set<String> scopes) {
    ClientApplicationAccessToken clientApplicationAccessToken = clientApplicationAccessTokenDAO.create(
        accessToken, 
        refreshToken, 
        clientApplicationAuthorizationCode.getClientApplication(), 
        clientApplicationAuthorizationCode.getUser(), 
        scopes);
    if (!clientApplicationAuthorizationCode.getUser().hasRole(Role.TRUSTED_SYSTEM)) {
      clientApplicationAuthorizationCodeDAO.delete(clientApplicationAuthorizationCode);
    }
    return clientApplicationAccessToken;
  }
  
  public ClientApplicationAuthorizationCode createAuthorizationCode(User user, ClientApplication clientApplication, String authorizationCode, String redirectUrl, Set<String> selectedScopes) {
    return clientApplicationAuthorizationCodeDAO.create(user, clientApplication, authorizationCode, redirectUrl, selectedScopes);
  }

  public ClientApplicationAccessToken findByAccessToken(String accessToken) {
    return clientApplicationAccessTokenDAO.findByAccessToken(accessToken);
  }
  
  public ClientApplicationAccessToken findByClientApplicationAndRefreshToken(ClientApplication clientApplication, String refreshToken) {
    return clientApplicationAccessTokenDAO.findByClientApplicationAndRefreshToken(clientApplication, refreshToken);
  }
  
  public ClientApplication findActiveByClientIdAndClientSecret(String clientId, String clientSecret) {
    return clientApplicationDAO.findActiveByClientIdAndClientSecret(clientId, clientSecret);
  }

  public ClientApplicationAuthorizationCode findByClientApplicationAndAuthorizationCode(ClientApplication clientApplication, String authorizationCode) {
    return clientApplicationAuthorizationCodeDAO.findByClientApplicationAndAuthorizationCode(authorizationCode, clientApplication);
  }

  public ClientApplicationAccessToken refreshToken(ClientApplicationAccessToken clientApplicationAccessToken, String newAccessToken, String newRefreshToken) {
    return clientApplicationAccessTokenDAO.refreshAccessToken(clientApplicationAccessToken, newAccessToken, newRefreshToken);
  }

  public void deleteAccessToken(ClientApplicationAccessToken clientApplicationAccessToken){
    clientApplicationAccessTokenDAO.delete(clientApplicationAccessToken);
  }

}
