package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.VariableKey;
import fi.otavanopisto.pyramus.rest.model.VariableType;
import io.restassured.response.Response;

public class SchoolVariablePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSchoolVariables(Role role) throws NoSuchFieldException {
    VariableKey schoolVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    assertOk(role, response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLVARIABLEKEY, 200);
    
    if (response.statusCode() == 200) {
      given().headers(getAdminAuthHeaders())
        .delete("/schools/variables/{KEY}", schoolVariable.getKey());
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSchoolVariables(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/variables"), schoolPermissions, SchoolPermissions.LIST_SCHOOLVARIABLEKEYS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSchoolVariable(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/variables/TV1"), schoolPermissions, SchoolPermissions.FIND_SCHOOLVARIABLEKEY, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateSchoolVariable(Role role) throws NoSuchFieldException {
    VariableKey schoolVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateVariable)
        .put("/schools/variables/{KEY}", updateVariable.getKey());
      assertOk(role, updateResponse, schoolPermissions, SchoolPermissions.UPDATE_SCHOOLVARIABLEKEY, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/schools/variables/{KEY}", schoolVariable.getKey());
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSchoolVariable(Role role) throws NoSuchFieldException {
    VariableKey schoolVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/schools/variables/{KEY}", schoolVariable.getKey());
    
    assertOk(role, deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLVARIABLEKEY, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/schools/variables/{KEY}", schoolVariable.getKey());
  }
}
