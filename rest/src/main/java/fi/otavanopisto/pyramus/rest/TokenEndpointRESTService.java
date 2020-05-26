package fi.otavanopisto.pyramus.rest;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.otavanopisto.pyramus.rest.annotation.Unsecure;
import fi.otavanopisto.pyramus.rest.controller.OauthController;

@Path("/oauth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Stateful
@RequestScoped
public class TokenEndpointRESTService extends AbstractRESTService {

  public static final long TOKEN_LIFETIME = 3600L;

  @Inject
  private Logger logger;

  @Inject
  private OauthController oauthController;

  @Unsecure
  @Path("/token")
  @POST
  public Response authorize(@Context HttpServletResponse res, @Context HttpServletRequest req)
      throws OAuthSystemException {

    OAuthTokenRequest oauthRequest;
    boolean refreshing = false;

    OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
    try {
      oauthRequest = new OAuthTokenRequest(req);

      ClientApplication clientApplication = oauthController.findByClientIdAndClientSecret(oauthRequest.getClientId(),
          oauthRequest.getClientSecret());

      if (clientApplication == null) {
        logger.severe("Invalid client application");
        OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_FORBIDDEN)
            .setError(OAuthError.TokenResponse.INVALID_CLIENT).setErrorDescription("Invalid client").buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
      }

      ClientApplicationAuthorizationCode clientApplicationAuthorizationCode = null;
      if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
        clientApplicationAuthorizationCode = oauthController
            .findByClientApplicationAndAuthorizationCode(clientApplication, oauthRequest.getParam(OAuth.OAUTH_CODE));
        if (clientApplicationAuthorizationCode == null) {
          logger.severe(String.format("Client application authorization code not found for token %s",
              oauthRequest.getParam(OAuth.OAUTH_CODE)));
          OAuthResponse response = OAuthASResponse
              .errorResponse(HttpServletResponse.SC_FORBIDDEN)
              .setError(OAuthError.TokenResponse.INVALID_GRANT)
              .setErrorDescription("invalid authorization code")
              .buildJSONMessage();
          return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
        }
      }
      else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
        refreshing = true;
      }
      else {
        return Response.status(HttpServletResponse.SC_NOT_IMPLEMENTED).build();
      }

      String accessToken = oauthIssuerImpl.accessToken();
      String refreshToken = oauthIssuerImpl.refreshToken();
      ClientApplicationAccessToken clientApplicationAccessToken;
      Long expires = (System.currentTimeMillis() / 1000L) + TOKEN_LIFETIME;

      if (refreshing) {
        // New access token and expiration time but refresh token remains unchanged
        refreshToken = oauthRequest.getParam(OAuth.OAUTH_REFRESH_TOKEN);
        clientApplicationAccessToken = oauthController.findByRefreshToken(refreshToken);
        if (clientApplicationAccessToken != null) {
          oauthController.refresh(clientApplicationAccessToken, expires, accessToken);
        }
        else {
          logger.severe(String.format("Invalid refresh token %s", refreshToken));
          OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_FORBIDDEN)
              .setError("Invalid refresh token").buildJSONMessage();
          return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
        }
      }
      else {
        clientApplicationAccessToken = oauthController
            .findByClientApplicationAuthorizationCode(clientApplicationAuthorizationCode);
        if (clientApplicationAccessToken == null) {
          oauthController.createAccessToken(
            accessToken,
            refreshToken,
            expires,
            clientApplication,
            clientApplicationAuthorizationCode);
        }
        else {
          oauthController.renewAccessToken(clientApplicationAccessToken, expires, accessToken, refreshToken);
        }
      }

      OAuthResponse response = OAuthASResponse
          .tokenResponse(HttpServletResponse.SC_OK)
          .setAccessToken(accessToken)
          .setRefreshToken(refreshToken)
          .setExpiresIn(String.valueOf(TOKEN_LIFETIME))
          .buildJSONMessage();
      return Response.status(response.getResponseStatus()).entity(response.getBody()).build();

    }
    catch (OAuthProblemException e) {
      logger.log(Level.SEVERE, "Oauth problem", e);
      OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
          .buildJSONMessage();
      return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }
  }

}
