package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class OauthTestsIT extends AbstractRESTServiceTest {
  
  @Test
  public void testSkipUserPrompt(){
    //TODO: how to do this?
  }
  
  @Test
  public void testRefreshToken(){
    OAuthClientRequest tokenRequest = null;
    try {
      tokenRequest = OAuthClientRequest
          .tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(fi.pyramus.Common.CLIENT_ID)
          .setClientSecret(fi.pyramus.Common.CLIENT_SECRET)
          .setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
          .setCode(fi.pyramus.Common.AUTH_CODE)
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
          .setClientId(fi.pyramus.Common.CLIENT_ID)
          .setClientSecret(fi.pyramus.Common.CLIENT_SECRET)
          .setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
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
