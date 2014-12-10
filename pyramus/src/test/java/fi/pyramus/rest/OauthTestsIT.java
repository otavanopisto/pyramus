package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

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
    
    login(2);
        
    OAuthClientRequest request = OAuthClientRequest
        .authorizationLocation(Common.AUTH_URL)
        .setClientId(Common.CLIENT_ID)
        .setRedirectURI(Common.REDIRECT_URL)
        .setResponseType(ResponseType.CODE.toString())
        .setState("state")
        .buildQueryMessage();
    
    Response htmlResponse = given()
        .header("Accept-Language", "en-US,en;q=0.5")
        //.redirects().follow(false)
        //.cookie("pyramusLocale", "fi_FI")
        .contentType(ContentType.ANY)
        .sessionId(getSessionId())
        .get(request.getLocationUri());
    
    OAuthClientRequest redirectRequest = OAuthClientRequest
        .authorizationLocation(Common.AUTH_URL)
        .setClientId(Common.SKIP_ID)
        .setRedirectURI(Common.REDIRECT_URL)
        .setResponseType(ResponseType.CODE.toString())
        .setState("state")
        .buildQueryMessage();
    
    Response redirectResponse = given()
        .header("Accept-Language", "en-US,en;q=0.5")
        //.redirects().follow(false)
        //.cookie("pyramusLocale", "fi_FI")
        .contentType(ContentType.ANY)
        .sessionId(getSessionId())
        .get("https://dev.pyramus.fi:8443/system/plugins.page#at-plugins");
    
    System.out.println(redirectResponse.asString());       
  
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
