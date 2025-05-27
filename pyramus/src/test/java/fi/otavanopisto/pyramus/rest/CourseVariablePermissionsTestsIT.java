package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.VariableKey;
import fi.otavanopisto.pyramus.rest.model.VariableType;
import io.restassured.response.Response;

public class CourseVariablePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateCourseVariables(Role role) throws NoSuchFieldException {
    VariableKey courseVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_COURSEBASEVARIABLEKEY, 200);
    if (response.statusCode() == 200) {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/variables/{KEY}", courseVariable.getKey());
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseVariables(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/variables");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_COURSEBASEVARIABLEKEYS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCourseVariable(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
    .get("/courses/variables/TV1");
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_COURSEBASEVARIABLEKEY, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseVariable(Role role) throws NoSuchFieldException {
    VariableKey courseVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    String key = courseVariable.getKey();
    
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateVariable)
        .put("/courses/variables/{KEY}", key);
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_COURSEBASEVARIABLEKEY, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/variables/{KEY}", key);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourseVariable(Role role) throws NoSuchFieldException {
    VariableKey courseVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
    .delete("/courses/variables/{KEY}", courseVariable.getKey());
    
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.DELETE_COURSEBASEVARIABLEKEY, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/courses/variables/{KEY}", courseVariable.getKey());
  }
}
