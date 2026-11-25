package fi.otavanopisto.pyramus.koski;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.AbstractRESTPermissionsTestJUnit5;
import fi.otavanopisto.pyramus.rest.controller.permissions.PyramusViewPermissions;
import io.restassured.response.Response;

public class KoskiPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private PyramusViewPermissions pyramusViewPermissions = new PyramusViewPermissions();

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindPersonOppija(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/koski/persons/{ID}/oppija", 3);

    int expectedStatusCode = role == Role.ADMINISTRATOR ? 200 : role == Role.EVERYONE ? 401 : 404;
    int statusCode = response.statusCode();
    
    assertEquals(String.format("Status code <%d> didn't match expected code <%d> when Role = %s", statusCode, expectedStatusCode, role),
        expectedStatusCode, statusCode);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindQueueStatus(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/koski/persons/{ID}/queuestatus", 3);

    assertOk(role, response, pyramusViewPermissions, PyramusViewPermissions.VIEW_STUDENT, 200, 404);
  }
  
}
