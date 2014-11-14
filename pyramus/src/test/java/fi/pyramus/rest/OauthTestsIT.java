package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import fi.pyramus.Common;

public class OauthTestsIT extends AbstractRESTServiceTest {
  
  @Test
  public void testSkipUserPrompt() throws OAuthSystemException{
    
    OAuthClientRequest noSkipReqWithEmptyAuthCode = null;
    try {
      noSkipReqWithEmptyAuthCode = OAuthClientRequest.tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(fi.pyramus.Common.CLIENT_ID)
          .setClientSecret(fi.pyramus.Common.CLIENT_SECRET)
          .setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
          .setCode("null").buildBodyMessage();
    } catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    Response forbiddenResponse = given()
        .contentType("application/x-www-form-urlencoded")
        .body(noSkipReqWithEmptyAuthCode.getBody())
        .post("/oauth/token");

    forbiddenResponse.then().statusCode(HttpServletResponse.SC_FORBIDDEN);
    
    OAuthClientRequest skipReqWithEmptyAuthCode = null;
    try {
      skipReqWithEmptyAuthCode = OAuthClientRequest.tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(fi.pyramus.Common.SKIP_ID)
          .setClientSecret(fi.pyramus.Common.SKIP_SECRET)
          .setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
          .setCode("null").buildBodyMessage();
    } catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    Response skipResponse = given()
        .contentType("application/x-www-form-urlencoded")
        .body(skipReqWithEmptyAuthCode.getBody())
        .post("/oauth/token");

    String accessToken = skipResponse.body().jsonPath().getString("access_token");
    assertNotNull(accessToken);     
  
  }
  
  @Test
  public void testRefreshToken(){
    OAuthClientRequest tokenRequest = null;
    try {
      tokenRequest = OAuthClientRequest
          .tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(Common.CLIENT_ID)
          .setClientSecret(Common.CLIENT_SECRET)
          .setRedirectURI(Common.REDIRECT_URL)
          .setCode(Common.AUTH_CODE)
          .buildBodyMessage();
    } catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    Response response = given()
        .contentType("application/x-www-form-urlencoded")
        .body(tokenRequest.getBody())
        .post("/oauth/token");
    
    String refreshToken = response.body().jsonPath().getString("refresh_token");
    
    OAuthClientRequest refreshRequest = null;
    try {
      refreshRequest = OAuthClientRequest
          .tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
          .setGrantType(GrantType.REFRESH_TOKEN)
          .setClientId(Common.CLIENT_ID)
          .setClientSecret(Common.CLIENT_SECRET)
          .setRedirectURI(Common.REDIRECT_URL)
          .setRefreshToken(refreshToken)
          .buildBodyMessage();
    } catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    Response refreshResponse = given()
        .contentType("application/x-www-form-urlencoded")
        .body(tokenRequest.getBody())
        .post("/oauth/token");   
   
    refreshResponse.then()
      .body("refresh_token", is(refreshToken));
  }

}
