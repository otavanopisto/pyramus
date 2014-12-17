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
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import com.jayway.restassured.response.Response;

import fi.pyramus.AbstractIntegrationTest;

public abstract class AbstractRESTServiceTest extends AbstractIntegrationTest {

  @Before
  public void setupRestAssured() {

    RestAssured.baseURI = getAppUrl(true) + "/1";
    RestAssured.port = getPortHttps();
    RestAssured.authentication = certificate(getKeystoreFile(), getKeystorePass());
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {

          @SuppressWarnings("rawtypes")
          @Override
          public com.fasterxml.jackson.databind.ObjectMapper create(Class cls, String charset) {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            objectMapper.registerModule(new JodaModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper;
          }
        }));

  }

  @Before
  public void createAccessToken() {

    OAuthClientRequest tokenRequest = null;
    try {
      tokenRequest = OAuthClientRequest.tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token").setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(fi.pyramus.Common.CLIENT_ID).setClientSecret(fi.pyramus.Common.CLIENT_SECRET).setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
          .setCode(fi.pyramus.Common.AUTH_CODE).buildBodyMessage();
    } catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    Response response = given().contentType("application/x-www-form-urlencoded").body(tokenRequest.getBody()).post("/oauth/token");

    String accessToken = response.body().jsonPath().getString("access_token");
    setAccessToken(accessToken);
    
    setUserId(new Long(given().headers(getAuthHeaders())
        .contentType("application/json")
        .get("/system/whoami")
        .body().jsonPath().getInt("id")));
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Map<String, String> getAuthHeaders() {
    OAuthClientRequest bearerClientRequest = null;
    try {
      bearerClientRequest = new OAuthBearerClientRequest("https://dev.pyramus.fi").setAccessToken(this.getAccessToken()).buildHeaderMessage();
    } catch (OAuthSystemException e) {
    }
    return bearerClientRequest.getHeaders();
  }

  public void login(int userid) {
    Response loginResponse = given() // Login first using dummy login method
        .contentType(ContentType.URLENC).parameter("testuserid", userid).post("https://dev.pyramus.fi:8443/users/externallogin.page");
    String jsessionId = loginResponse.getCookie("JSESSIONID");
    setSessionId(jsessionId);
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public Long getUserId() {
    return userId;
  }
  
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  private String sessionId;
  private String accessToken;
  private Long userId;
}
