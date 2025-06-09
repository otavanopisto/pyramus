package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.VariableKey;
import fi.otavanopisto.pyramus.rest.model.VariableType;
import io.restassured.response.Response;

public class ModuleVariablePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateModuleVariables(Role role) throws NoSuchFieldException {
    VariableKey moduleVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(moduleVariable)
      .post("/modules/variables");
    
    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_COURSEBASEVARIABLEKEY, 200);
    
    if (response.statusCode() == 200) {
      given().headers(getAdminAuthHeaders())
        .delete("/modules/variables/{KEY}", moduleVariable.getKey());
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListModuleVariables(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/modules/variables");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_COURSEBASEVARIABLEKEYS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindModuleVariable(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
    .get("/modules/variables/TV1");
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_COURSEBASEVARIABLEKEY, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateModuleVariable(Role role) throws NoSuchFieldException {
    VariableKey moduleVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(moduleVariable)
      .post("/modules/variables");
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateVariable)
        .put("/modules/variables/{KEY}", updateVariable.getKey());
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_COURSEBASEVARIABLEKEY, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/modules/variables/{KEY}", moduleVariable.getKey());
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteModuleVariable(Role role) throws NoSuchFieldException {
    VariableKey moduleVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(moduleVariable)
      .post("/modules/variables");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/modules/variables/{KEY}", moduleVariable.getKey());
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.DELETE_COURSEBASEVARIABLEKEY, 204);
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/modules/variables/{KEY}", moduleVariable.getKey());
  }
}
