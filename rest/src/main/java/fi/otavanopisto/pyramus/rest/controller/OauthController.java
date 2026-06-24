package fi.otavanopisto.pyramus.rest.controller;

import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAccessTokenDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
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

  public ClientApplicationAccessToken createAccessToken(String accessToken, String refreshToken, Long expires, ClientApplication clientApplication, ClientApplicationAuthorizationCode clientApplicationAuthorizationCode, Set<String> scopes) {
    return clientApplicationAccessTokenDAO.create(accessToken, refreshToken, expires, clientApplication, clientApplicationAuthorizationCode, scopes);
  }
  
  public ClientApplicationAuthorizationCode createAuthorizationCode(User user, ClientApplication clientApplication, String authorizationCode, String redirectUrl, Set<String> selectedScopes) {
    return clientApplicationAuthorizationCodeDAO.create(user, clientApplication, authorizationCode, redirectUrl, selectedScopes);
  }

  public ClientApplicationAccessToken findByAccessToken(String accessToken) {
    return clientApplicationAccessTokenDAO.findByAccessToken(accessToken);
  }
  
  public ClientApplicationAccessToken findByRefreshToken(String refreshToken) {
    return clientApplicationAccessTokenDAO.findByRefreshToken(refreshToken);
  }
  
  public ClientApplication findActiveByClientIdAndClientSecret(String clientId, String clientSecret) {
    return clientApplicationDAO.findActiveByClientIdAndClientSecret(clientId, clientSecret);
  }

  public ClientApplicationAuthorizationCode findByClientApplicationAndAuthorizationCode(ClientApplication clientApplication, String authorizationCode) {
    return clientApplicationAuthorizationCodeDAO.findByClientApplicationAndAuthorizationCode(authorizationCode, clientApplication);
  }
  
  public ClientApplicationAccessToken findByClientApplicationAuthorizationCode(ClientApplicationAuthorizationCode clientApplicationAuthorizationCode){
    return clientApplicationAccessTokenDAO.findByAuthCode(clientApplicationAuthorizationCode);
  }
  
  public ClientApplicationAccessToken refresh(ClientApplicationAccessToken clientApplicationAccessToken, Long expires, String accessToken){
    clientApplicationAccessToken = clientApplicationAccessTokenDAO.updateAccessToken(clientApplicationAccessToken, accessToken);
    clientApplicationAccessToken = clientApplicationAccessTokenDAO.updateExpires(clientApplicationAccessToken, expires);
    return clientApplicationAccessToken;
  }

  public ClientApplicationAccessToken renewAccessToken(ClientApplicationAccessToken clientApplicationAccessToken, Long expires, String accessToken, String refreshToken) {
    clientApplicationAccessToken = clientApplicationAccessTokenDAO.updateRefreshToken(clientApplicationAccessToken, refreshToken);
    clientApplicationAccessToken = clientApplicationAccessTokenDAO.updateAccessToken(clientApplicationAccessToken, accessToken);
    clientApplicationAccessToken = clientApplicationAccessTokenDAO.updateExpires(clientApplicationAccessToken, expires);
    return clientApplicationAccessToken;
  }
  
  public void deleteAccessToken(ClientApplicationAccessToken clientApplicationAccessToken){
    clientApplicationAccessTokenDAO.delete(clientApplicationAccessToken);
  }
  

}
