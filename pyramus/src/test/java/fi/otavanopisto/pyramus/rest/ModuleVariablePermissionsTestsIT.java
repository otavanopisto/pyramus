package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.VariableKey;
import fi.otavanopisto.pyramus.rest.model.VariableType;

@RunWith(Parameterized.class)
public class ModuleVariablePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public ModuleVariablePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateModuleVariables() throws NoSuchFieldException {
    VariableKey moduleVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(moduleVariable)
      .post("/modules/variables");
    
    assertOk(response, commonPermissions, CommonPermissions.CREATE_COURSEBASEVARIABLEKEY, 200);
    
    Long statusCode = new Long(response.statusCode());
    if(statusCode.toString().equals("200")){
      given().headers(getAdminAuthHeaders())
      .delete("/modules/variables/{KEY}", moduleVariable.getKey());
    }
  }

  @Test
  public void testPermissionsListModuleVariables() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/modules/variables");
    assertOk(response, commonPermissions, CommonPermissions.LIST_COURSEBASEVARIABLEKEYS, 200);
  }
  
  @Test
  public void testPermissionsFindModuleVariable() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
    .get("/modules/variables/TV1");
    assertOk(response, commonPermissions, CommonPermissions.FIND_COURSEBASEVARIABLEKEY, 200);
  }
  
  @Test
  public void testPermissionsUpdateModuleVariable() throws NoSuchFieldException {
    VariableKey moduleVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(moduleVariable)
      .post("/modules/variables");
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateVariable)
        .put("/modules/variables/{KEY}", updateVariable.getKey());
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_COURSEBASEVARIABLEKEY, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/modules/variables/{KEY}", moduleVariable.getKey());
    }
  }
  
  @Test
  public void testDeleteModuleVariable() throws NoSuchFieldException {
    VariableKey moduleVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(moduleVariable)
      .post("/modules/variables");

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/modules/variables/{KEY}", moduleVariable.getKey());
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_COURSEBASEVARIABLEKEY, 204);
    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.toString().equals("204"))
      given().headers(getAdminAuthHeaders()).delete("/modules/variables/{KEY}", moduleVariable.getKey());
  }
}
