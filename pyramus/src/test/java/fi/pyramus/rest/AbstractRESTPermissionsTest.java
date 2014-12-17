package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.certificate;
import static com.jayway.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import fi.pyramus.Common;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.security.impl.PyramusPermissionCollection;

public abstract class AbstractRESTPermissionsTest extends AbstractIntegrationTest {

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
  public void createAccessTokens() {

    OAuthClientRequest tokenRequest = null;

    if (!Role.EVERYONE.name().equals(role)) {
      try {
        tokenRequest = OAuthClientRequest.tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
            .setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(fi.pyramus.Common.CLIENT_ID)
            .setClientSecret(fi.pyramus.Common.CLIENT_SECRET).setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
            .setCode(fi.pyramus.Common.ROLEAUTHS.get(role)).buildBodyMessage();
      } catch (OAuthSystemException e) {
        e.printStackTrace();
      }
      Response response = given().contentType("application/x-www-form-urlencoded").body(tokenRequest.getBody())
          .post("/oauth/token");
      String accessToken = response.body().jsonPath().getString("access_token");
      setAccessToken(accessToken);
    } else {
      setAccessToken("");
    }

    /**
     * AdminAccessToken
     */
    if (!Role.ADMINISTRATOR.name().equals(role)) {
      tokenRequest = null;
      try {
        tokenRequest = OAuthClientRequest.tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
            .setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(fi.pyramus.Common.CLIENT_ID)
            .setClientSecret(fi.pyramus.Common.CLIENT_SECRET).setRedirectURI(fi.pyramus.Common.REDIRECT_URL)
            .setCode(fi.pyramus.Common.ROLEAUTHS.get("ADMINISTRATOR")).buildBodyMessage();
      } catch (OAuthSystemException e) {
        e.printStackTrace();
      }
      Response response = given().contentType("application/x-www-form-urlencoded").body(tokenRequest.getBody())
          .post("/oauth/token");
  
      String adminAccessToken = response.body().jsonPath().getString("access_token");
      setAdminAccessToken(adminAccessToken);
    } else {
      setAdminAccessToken(accessToken);
    }
  }
  
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  
  public String getAdminAccessToken() {
    return adminAccessToken;
  }

  public void setAdminAccessToken(String adminAccesToken) {
    this.adminAccessToken = adminAccesToken;
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
  
  public Map<String, String> getAdminAuthHeaders() {
    OAuthClientRequest bearerClientRequest = null;
    try {
      bearerClientRequest = new OAuthBearerClientRequest("https://dev.pyramus.fi")
          .setAccessToken(this.getAdminAccessToken()).buildHeaderMessage();
    } catch (OAuthSystemException e) {
    }
    return bearerClientRequest.getHeaders();
  }

  public Long getUserIdForRole(String role) {
    // TODO: could this use the /system/whoami end-point?
    return Common.ROLEUSERS.get(role);
  }
  
  public boolean roleIsAllowed(String role, List<String> allowedRoles) {
    // Everyone -> every role has access
    if (allowedRoles.contains(Role.EVERYONE.name()))
      return true;
    
    for (String str : allowedRoles) {
      if (str.equals(role)) {
        return true;
      }
    }
    return false;
  }

  public void assertOk(String path, List<String> allowedRoles) {
    if (!Role.EVERYONE.name().equals(getRole())) {
      if (roleIsAllowed(getRole(), allowedRoles)) {
        given().headers(getAuthHeaders()).get(path).then().assertThat().statusCode(200);
      } else {
        given().headers(getAuthHeaders()).get(path).then().assertThat().statusCode(403);
      }
    }
    else
      given().headers(getAuthHeaders()).get(path).then().assertThat().statusCode(400);
  }

  public void assertOk(Response response, PyramusPermissionCollection permissionCollection, String permission) throws NoSuchFieldException {
    assertOk(response, permissionCollection, permission, 200);
  }
  
  public void assertOk(Response response, PyramusPermissionCollection permissionCollection, String permission, int successStatusCode) throws NoSuchFieldException {
    if (!Role.EVERYONE.name().equals(getRole())) {
      List<String> allowedRoles = Arrays.asList(permissionCollection.getDefaultRoles(permission));
      
      if (roleIsAllowed(getRole(), allowedRoles)) {
        response.then().assertThat().statusCode(successStatusCode);
      } else {
        response.then().assertThat().statusCode(403);
      }
    }
    else
      response.then().assertThat().statusCode(400);
  }
  
  public static List<Object[]> getGeneratedRoleData() {
    // The parameter generator returns a List of
    // arrays. Each array has two elements: { role }.
    
    List<Object[]> data = new ArrayList<Object[]>();
    
    for (Role role : Role.values()) {
      data.add(new Object[] { 
        role.name() 
      });
    }
    
    return data;
    
//    return Arrays.asList(new Object[][] {
//        { Role.EVERYONE.name() },
//        { Role.GUEST.name() },
//        { Role.USER.name() },
//        { Role.STUDENT.name() },
//        { Role.MANAGER.name() },
//        { Role.ADMINISTRATOR.name() }
//      }
//    );
  }
  
  protected String getRole() {
    return role;
  }

  protected void setRole(String role) {
    this.role = role;
  }

  protected String role;
  private String accessToken;
  private String adminAccessToken;
}
