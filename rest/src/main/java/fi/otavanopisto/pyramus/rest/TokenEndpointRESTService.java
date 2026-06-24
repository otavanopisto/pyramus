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

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.OAuth2Error;
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

  public static final long TOKEN_LIFETIME = 3600L;

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

      // TODO Refresh token ?
//    clientApplicationAccessToken = oauthController.findByRefreshToken(refreshToken);
//    if (clientApplicationAccessToken != null) {
//      oauthController.refresh(clientApplicationAccessToken, expires, accessToken);
//    }
      
      if (authorizationGrant instanceof AuthorizationCodeGrant) {
        AuthorizationCodeGrant authorizationCodeGrant = (AuthorizationCodeGrant) authorizationGrant;
        String authorizationCode = authorizationCodeGrant.getAuthorizationCode().getValue();

        ClientAuthentication clientAuthentication = tokenRequest.getClientAuthentication();
        
        if (clientAuthentication instanceof PlainClientSecret) {
          PlainClientSecret clientCredentials = (PlainClientSecret) clientAuthentication;
          
          ClientApplication clientApplication = oauthController.findActiveByClientIdAndClientSecret(clientCredentials.getClientID().getValue(),
              clientCredentials.getClientSecret().getValue());
          if (clientApplication == null) {
            return oauthTokenError(OAuth2Error.INVALID_CLIENT);
          }
          
          ClientApplicationAuthorizationCode clientApplicationAuthorizationCode = oauthController
              .findByClientApplicationAndAuthorizationCode(clientApplication, authorizationCode);
          if (clientApplicationAuthorizationCode == null) {
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
                return oauthTokenError(OAuth2Error.INVALID_SCOPE);
              }
            }
          }

          // Make sure the scopes are also allowed for the client application
          for (String grantedScope : grantedScopes) {
            if (!clientApplication.getScopes().contains(grantedScope)) {
              return oauthTokenError(OAuth2Error.INVALID_SCOPE);
            }
          }
          
          // Create token

          Scope scope = new Scope(grantedScopes.toArray(new String[0]));
          AccessToken accessToken = new BearerAccessToken(TOKEN_LIFETIME, scope);
          RefreshToken refreshToken = new RefreshToken();
          Long expires = (System.currentTimeMillis() / 1000L) + TOKEN_LIFETIME;
          
          oauthController.createAccessToken(
              accessToken.getValue(),
              refreshToken.getValue(),
              expires,
              clientApplication,
              clientApplicationAuthorizationCode,
              grantedScopes
              );
          
          // Delete AuthorizationCode // TODO
          
          // Send response
          
          AccessTokenResponse accessTokenResponse = new AccessTokenResponse(new Tokens(accessToken, refreshToken));
  
          String body = accessTokenResponse.toJSONObject().toString();

          CacheControl cacheControl = new CacheControl();
          cacheControl.setNoCache(true);

          return Response.ok().entity(body).cacheControl(cacheControl).build();
        }
        else {
          // Not a plain secret request
          return oauthTokenError(OAuth2Error.INVALID_CLIENT);
        }
      }
      else {
        // Not an authorization code grant
        return oauthTokenError(OAuth2Error.UNSUPPORTED_GRANT_TYPE);
      }
      
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Couldn't handle token request", e);
      return oauthTokenError(OAuth2Error.SERVER_ERROR);
    }
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
