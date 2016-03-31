package fi.otavanopisto.pyramus.rest;

public class OauthTestsIT extends AbstractRESTServiceTest {
  /* TODO: implement proper tests
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
*/
}
