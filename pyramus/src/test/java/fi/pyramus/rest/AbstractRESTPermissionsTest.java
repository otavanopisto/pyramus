package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.certificate;
import static com.jayway.restassured.RestAssured.given;

import java.util.Map;

import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.junit.Before;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import com.jayway.restassured.response.Response;

import fi.pyramus.AbstractIntegrationTest;

public abstract class AbstractRESTPermissionsTest extends AbstractIntegrationTest {
  
  @Before
  public void setupRestAssured() {
    
    RestAssured.baseURI = getAppUrl(true) + "/1";
    RestAssured.port = getPortHttps();
    RestAssured.authentication = certificate(getKeystoreFile(), getKeystorePass());
    
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(
      new Jackson2ObjectMapperFactory() {
        
        @SuppressWarnings("rawtypes")
        @Override
        public com.fasterxml.jackson.databind.ObjectMapper create(Class cls, String charset) {
          com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
          objectMapper.registerModule(new JodaModule());
          objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
          return objectMapper;
        }
      }
    ));
    
  }
  
  @Before
  public void createAccessToken(){
    OAuthClientRequest tokenRequest = null;
    try {
      tokenRequest = OAuthClientRequest
          .tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(fi.pyramus.Common.CLIENT_ID)
          .setClientSecret(fi.pyramus.Common.CLIENT_SECRET)
          .setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
          .setCode(fi.pyramus.Common.ROLEAUTHS.get(role))
          .buildBodyMessage();
    } catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    Response response = given()
        .contentType("application/x-www-form-urlencoded")
        .body(tokenRequest.getBody())
        .post("/oauth/token");
    
    String accessToken = response.body().jsonPath().getString("access_token");
    setAccessToken(accessToken);
  }

  public String getAccessToken() {
    return AccessToken;
  }

  public void setAccessToken(String accessToken) {
    AccessToken = accessToken;
  }
  
  public Map<String, String> getAuthHeaders() {
    OAuthClientRequest bearerClientRequest = null;
    try {
      bearerClientRequest = new OAuthBearerClientRequest("https://dev.pyramus.fi")
      .setAccessToken(this.getAccessToken()).buildHeaderMessage();
    } catch (OAuthSystemException e) {
    }
    return bearerClientRequest.getHeaders();
  }
  
  protected String getRole() {
    return this.role;
  }

  protected void setRole(String role) {
     this.role = role;
  }
  
  private String role;
  private String AccessToken;
  
}
