package fi.otavanopisto.pyramus.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.OAuth2Error;
import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.Scope.Value;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.PlainClientSecret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.ServletUtils;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.otavanopisto.pyramus.rest.annotation.AuthScope;
import fi.otavanopisto.pyramus.rest.annotation.Unsecure;
import fi.otavanopisto.pyramus.rest.controller.OauthController;

@Path("/oauth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Stateful
@RequestScoped
@AuthScope(AuthScope.LEGACY)
public class TokenEndpointRESTService extends AbstractRESTService {

  @Inject
  private Logger logger;

  @Inject
  private OauthController oauthController;

  @Unsecure
  @Path("/token")
  @POST
  public Response authorize(@Context HttpServletResponse res, @Context HttpServletRequest req) {
    try {
      HTTPRequest httpRequest = ServletUtils.createHTTPRequest(req);
      TokenRequest tokenRequest = TokenRequest.parse(httpRequest);

      AuthorizationGrant authorizationGrant = tokenRequest.getAuthorizationGrant();

      ClientApplication clientApplication = getClientApplication(tokenRequest);
      if (clientApplication == null) {
        logger.log(Level.FINE, "Token request rejected due to invalid client.");
        return oauthTokenError(OAuth2Error.INVALID_CLIENT);
      }

      if (authorizationGrant instanceof AuthorizationCodeGrant) {
        AuthorizationCodeGrant authorizationCodeGrant = (AuthorizationCodeGrant) authorizationGrant;
        String authorizationCode = authorizationCodeGrant.getAuthorizationCode().getValue();

        ClientApplicationAuthorizationCode clientApplicationAuthorizationCode = oauthController
            .findByClientApplicationAndAuthorizationCode(clientApplication, authorizationCode);
        if (clientApplicationAuthorizationCode == null || !clientApplicationAuthorizationCode.isValidAuthorizationCode()) {
          logger.log(Level.FINE, "Token request rejected due to non-existing or expired authorization code.");
          return oauthTokenError(OAuth2Error.INVALID_GRANT);
        }

        // Check requested scopes
        
        Scope requestedScopes = tokenRequest.getScope();
        Set<String> grantedScopes = new HashSet<>();
        
        if (requestedScopes == null || requestedScopes.isEmpty()) {
          // Grant the scopes from the authorization code by default if the request doesn't 
          // specify any scopes because the platform requires at least one scope to be present
          grantedScopes.addAll(clientApplicationAuthorizationCode.getSelectedScopes());
        }
        else {
          for (Value requestedScope : requestedScopes) {
            String requestedScopeStr = requestedScope.getValue();
            if (clientApplicationAuthorizationCode.getSelectedScopes().contains(requestedScopeStr)) {
              grantedScopes.add(requestedScopeStr);
            }
            else {
              logger.log(Level.FINE, "Token request rejected due to requested scope not being authorized by the user.");
              return oauthTokenError(OAuth2Error.INVALID_SCOPE);
            }
          }
        }

        // Make sure the scopes are also allowed for the client application
        for (String grantedScope : grantedScopes) {
          if (!clientApplication.getScopes().contains(grantedScope)) {
            logger.log(Level.FINE, "Token request rejected due to requested scope not being available to the client application.");
            return oauthTokenError(OAuth2Error.INVALID_SCOPE);
          }
        }
        
        // Create token

        Scope scope = new Scope(grantedScopes.toArray(new String[0]));
        AccessToken accessToken = new BearerAccessToken(ClientApplicationAccessToken.ACCESSTOKEN_LIFETIME.getSeconds(), scope);
        RefreshToken refreshToken = new RefreshToken();
        
        logger.log(Level.FINE, String.format("Authorization code grant from client %s with authorization code %s. Granted access code %s... with refresh token %s...", 
            clientApplication.getClientName(), authorizationCode.substring(0, 4), accessToken.getValue().substring(0, 4), refreshToken.getValue().substring(0, 4)));
        
        oauthController.tradeAuthorizationCodeForAccessToken(
            clientApplicationAuthorizationCode,
            accessToken.getValue(),
            refreshToken.getValue(),
            grantedScopes);
        
        // Send response
        
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(new Tokens(accessToken, refreshToken));

        String body = accessTokenResponse.toJSONObject().toString();

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);

        return Response.ok().entity(body).cacheControl(cacheControl).build();
      }
      else if (authorizationGrant instanceof RefreshTokenGrant) {
        RefreshTokenGrant refreshTokenGrant = (RefreshTokenGrant) authorizationGrant;
        
        String refreshToken = refreshTokenGrant.getRefreshToken().getValue();
        
        if (StringUtils.isBlank(refreshToken)) {
          logger.log(Level.FINE, "Refreshing access token with failed as the refresh token was blank.");
          return oauthTokenError(OAuth2Error.INVALID_GRANT);
        }

        ClientApplicationAccessToken clientApplicationAccessToken = oauthController.findByClientApplicationAndRefreshToken(clientApplication, refreshToken);
        if (clientApplicationAccessToken == null) {
          logger.log(Level.FINE, String.format("Refreshing access token with refresh token %s... failed as the token doesn't exist.", refreshToken.substring(0, 4)));
          return oauthTokenError(OAuth2Error.INVALID_GRANT);
        }
        else if (!clientApplicationAccessToken.isValidRefreshToken()) {
          logger.log(Level.FINE, String.format("Refreshing access token with refresh token %s... refused as the refresh token has expired.", refreshToken.substring(0, 4)));
          return oauthTokenError(OAuth2Error.INVALID_GRANT);
        }
        else {
          // This uses the original scopes of the token although the spec implies
          // they could change upon request. Consider this later if needed.
          Scope scope = new Scope(clientApplicationAccessToken.getScopes().toArray(new String[0]));
          AccessToken newAccessToken = new BearerAccessToken(ClientApplicationAccessToken.ACCESSTOKEN_LIFETIME.getSeconds(), scope);
          RefreshToken newRefreshToken = new RefreshToken();
          
          logger.log(Level.FINE, String.format("Refreshing access token %s... with refresh token %s.... New tokens %s... %s....", 
              clientApplicationAccessToken.getAccessToken().substring(0, 4),
              clientApplicationAccessToken.getRefreshToken().substring(0, 4),
              newAccessToken.getValue().substring(0, 4),
              newRefreshToken.getValue().substring(0, 4)));
          
          oauthController.refreshToken(clientApplicationAccessToken, newAccessToken.getValue(), newRefreshToken.getValue());
          
          // Send response
          
          AccessTokenResponse accessTokenResponse = new AccessTokenResponse(new Tokens(newAccessToken, newRefreshToken));
  
          String body = accessTokenResponse.toJSONObject().toString();

          CacheControl cacheControl = new CacheControl();
          cacheControl.setNoCache(true);

          return Response.ok().entity(body).cacheControl(cacheControl).build();
        }
      }
      else {
        // Not an authorization code grant nor refresh token grant
        return oauthTokenError(OAuth2Error.UNSUPPORTED_GRANT_TYPE);
      }
      
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Couldn't handle token request", e);
      return oauthTokenError(OAuth2Error.SERVER_ERROR);
    }
  }

  private ClientApplication getClientApplication(TokenRequest tokenRequest) {
    ClientAuthentication clientAuthentication = tokenRequest.getClientAuthentication();
    
    if (clientAuthentication instanceof PlainClientSecret) {
      PlainClientSecret clientCredentials = (PlainClientSecret) clientAuthentication;
      
      return oauthController.findActiveByClientIdAndClientSecret(clientCredentials.getClientID().getValue(),
          clientCredentials.getClientSecret().getValue());
    }

    return null;
  }

  private Response oauthTokenError(ErrorObject error) {
    TokenErrorResponse tokenErrorResponse = new TokenErrorResponse(error);
    String body = tokenErrorResponse.toJSONObject().toString();
    
    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoCache(true);
    
    return Response
        .status(error.getHTTPStatusCode())
        .entity(body)
        .cacheControl(cacheControl)
        .build();
  }

}
