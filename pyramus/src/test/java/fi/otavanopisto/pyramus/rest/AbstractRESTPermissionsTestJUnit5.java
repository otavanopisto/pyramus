package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;

import fi.otavanopisto.pyramus.AbstractIntegrationTest;
import fi.otavanopisto.pyramus.Common;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import io.restassured.response.Response;

public abstract class AbstractRESTPermissionsTestJUnit5 extends AbstractIntegrationTest implements AbstractRestServicePermissionsTestI {

  public AbstractRESTPermissionsTestJUnit5() {
    this.tools = new AbstractRESTServiceTestTools(this);
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
    Map<String, String> headers = new HashMap<>();
    String accessToken = this.getOauthToken(role);
    if (StringUtils.isNotBlank(accessToken)) {
      headers.put("Authorization", "Bearer " + accessToken);
    }
    return headers;
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
    // Cannot access endpoints without logging in
    if (role == Role.EVERYONE) {
      return false;
    }
    
    // Everyone -> every role has access
    if (allowedRoles.contains(Role.EVERYONE.name())) {
      return true;
    }

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
    return !Role.EVERYONE.equals(role) ? getOAuthAccessToken(role) : "";
  }
  
  protected AbstractRESTServiceTestTools tools() {
    return tools;
  }
  
  private AbstractRESTServiceTestTools tools;
}
