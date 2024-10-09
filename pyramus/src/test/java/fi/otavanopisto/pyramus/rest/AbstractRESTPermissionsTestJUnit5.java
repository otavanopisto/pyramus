package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.certificate;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.junit.jupiter.api.BeforeAll;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.otavanopisto.pyramus.AbstractIntegrationTest;
import fi.otavanopisto.pyramus.Common;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;

public abstract class AbstractRESTPermissionsTestJUnit5 extends AbstractIntegrationTest implements AbstractRestServicePermissionsTestI {

  public AbstractRESTPermissionsTestJUnit5() {
    this.tools = new AbstractRESTServiceTestTools(this);
  }
  
  static {
    RestAssured.baseURI = getAppUrl(true) + "/1";
    RestAssured.port = getPortHttps();
    RestAssured.useRelaxedHTTPSValidation();
    RestAssured.authentication = certificate(getKeystoreFile(), getKeystorePass());

    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
          @Override
          public com.fasterxml.jackson.databind.ObjectMapper create(Type cls, String charset) {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper;
          }
        }));
  }
  
  @BeforeAll
  public static void testConnection() throws IOException {
    Socket socket = new Socket();
    try {
      socket.connect(new InetSocketAddress(getHost(), getPortHttp()), 0);
    } catch (IOException e) {
      throw new AssertionError("Could not establish connection to server!");
    } finally {
      socket.close();
    }
  }

  public Map<String, String> getAdminAuthHeaders() {
    return getAuthHeaders(Role.ADMINISTRATOR);
  }

  public Map<String, String> getAuthHeaders(Role role) {
    OAuthClientRequest bearerClientRequest = null;
    try {
      bearerClientRequest = new OAuthBearerClientRequest("https://dev.pyramus.fi").setAccessToken(this.getOauthToken(role))
          .buildHeaderMessage();
    } catch (OAuthSystemException e) {
    }

    return bearerClientRequest.getHeaders();
  }

  public Long getUserIdForRole(Role role) {
    // TODO: could this use the /system/whoami end-point?
    return Common.getUserId(role);
  }

  public boolean roleIsAllowed(Role role, PyramusPermissionCollection permissionCollection, String permission) throws NoSuchFieldException {
    List<String> allowedRoles = Arrays.asList(permissionCollection.getDefaultRoles(permission));

    return roleIsAllowed(role, allowedRoles);
  }
	
  public boolean roleIsAllowed(Role role, List<String> allowedRoles) {
    // Everyone -> every role has access
    if (allowedRoles.contains(Role.EVERYONE.name()))
      return true;

    return allowedRoles.contains(role.name());
  }

  public void assertOk(Role role, String path, List<String> allowedRoles) {
    if (roleIsAllowed(role, allowedRoles)) {
      given().headers(getAuthHeaders(role)).get(path).then().assertThat().statusCode(200);
    } else {
      given().headers(getAuthHeaders(role)).get(path).then().assertThat().statusCode(403);
    }
  }

  public void assertOk(Role role, Response response, PyramusPermissionCollection permissionCollection, String permission)
      throws NoSuchFieldException {
    assertOk(role, response, permissionCollection, permission, 200);
  }

  public void assertOk(Role role, Response response, PyramusPermissionCollection permissionCollection, String permission,
      int successStatusCode) throws NoSuchFieldException {
    int expectedStatusCode = roleIsAllowed(role, permissionCollection, permission) ? successStatusCode : 403;

    assertPermission(role, permission, expectedStatusCode, response.statusCode());
  }

  public void assertPermission(Role role, String permission, int expectedStatusCode, int statusCode) throws NoSuchFieldException {
    assertEquals(expectedStatusCode, statusCode, String.format("Status code <%d> didn't match expected code <%d> when Role = %s, Permission = %s",
        statusCode, expectedStatusCode, role, permission));
  }
  
  protected String getOauthToken(Role role) {
    if (!Role.EVERYONE.equals(role)) {
      OAuthClientRequest tokenRequest = null;
      try {
        tokenRequest = OAuthClientRequest.tokenLocation("https://dev.pyramus.fi:8443/1/oauth/token")
            .setGrantType(GrantType.AUTHORIZATION_CODE)
            .setClientId(fi.otavanopisto.pyramus.Common.CLIENT_ID)
            .setClientSecret(fi.otavanopisto.pyramus.Common.CLIENT_SECRET)
            .setRedirectURI(fi.otavanopisto.pyramus.Common.REDIRECT_URL)
            .setCode(fi.otavanopisto.pyramus.Common.getRoleAuth(role)).buildBodyMessage();
      } catch (OAuthSystemException e) {
        e.printStackTrace();
      }

      Response response = given().contentType("application/x-www-form-urlencoded").body(tokenRequest.getBody())
          .post("/oauth/token");
      return response.body().jsonPath().getString("access_token");
    }
    
    return "";
  }
  
  protected AbstractRESTServiceTestTools tools() {
    return tools;
  }
  
  private AbstractRESTServiceTestTools tools;
}
