package fi.otavanopisto.pyramus.rest.oauth;

import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;

import jakarta.enterprise.context.Dependent;

@Dependent
public class PyramusOAuthDataProvider implements OAuthDataProvider {

  @Override
  public Client getClient(String clientId) throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("getClient");
    return null;
  }

  @Override
  public ServerAccessToken createAccessToken(AccessTokenRegistration accessToken) throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("createAccessToken");
    return null;
  }

  @Override
  public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("getAccessToken");
    return null;
  }

  @Override
  public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes, UserSubject subject,
      String grantType) throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("getPreauthorizedToken");
    return null;
  }

  @Override
  public ServerAccessToken refreshAccessToken(Client client, String refreshToken, List<String> requestedScopes)
      throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("refreshAccessToken");
    return null;
  }

  @Override
  public List<ServerAccessToken> getAccessTokens(Client client, UserSubject subject) throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("getAccessTokens");
    return null;
  }

  @Override
  public List<RefreshToken> getRefreshTokens(Client client, UserSubject subject) throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("getRefreshTokens");
    return null;
  }

  @Override
  public void revokeToken(Client client, String tokenId, String tokenTypeHint) throws OAuthServiceException {
    // TODO Auto-generated method stub
    System.out.println("revokeToken");

  }

  @Override
  public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScopes) {
    // TODO Auto-generated method stub
    System.out.println("convertScopeToPermissions");
    return null;
  }

}
