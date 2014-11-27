package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.pyramus.rest.model.VariableKey;
import fi.pyramus.rest.model.VariableType;

@RunWith(Parameterized.class)
public class SchoolVariablePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public SchoolVariablePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSchoolVariables() throws NoSuchFieldException {
    VariableKey schoolVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    assertOk(response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLVARIABLEKEY, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.equals(200)){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAuthHeaders())
        .delete("/schools/variables/{KEY}", schoolVariable.getKey());
      }
    }
  }

  @Test
  public void testPermissionsListSchoolVariables() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/variables"), schoolPermissions, SchoolPermissions.LIST_SCHOOLVARIABLEKEYS, 200);
  }
  
  @Test
  public void testPermissionsFindSchoolVariable() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/variables/TV1"), schoolPermissions, SchoolPermissions.FIND_SCHOOLVARIABLEKEY, 200);
  }
  
  @Test
  public void testPermissionsUpdateSchoolVariable() throws NoSuchFieldException {
    VariableKey schoolVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateVariable)
        .put("/schools/variables/{KEY}", updateVariable.getKey());
      assertOk(updateResponse, schoolPermissions, SchoolPermissions.UPDATE_SCHOOLVARIABLEKEY, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/schools/variables/{KEY}", schoolVariable.getKey());
    }
  }
  
  @Test
  public void testPermissionsDeleteSchoolVariable() throws NoSuchFieldException {
    VariableKey schoolVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/schools/variables/{KEY}", schoolVariable.getKey());
    
    assertOk(deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLVARIABLEKEY, 204);
    
    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.equals(204))
      given().headers(getAdminAuthHeaders())
      .delete("/schools/variables/{KEY}", schoolVariable.getKey());
  }
}
