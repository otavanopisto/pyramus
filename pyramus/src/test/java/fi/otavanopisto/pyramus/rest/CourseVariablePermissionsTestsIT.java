package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.VariableKey;
import fi.otavanopisto.pyramus.rest.model.VariableType;

@RunWith(Parameterized.class)
public class CourseVariablePermissionsTestsIT extends AbstractRESTPermissionsTest {
  
  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseVariablePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testCreateCourseVariables() throws NoSuchFieldException {
    VariableKey courseVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    assertOk(response, commonPermissions, CommonPermissions.CREATE_COURSEBASEVARIABLEKEY, 200);
    if (response.statusCode() == 200) {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/variables/{KEY}", courseVariable.getKey());
    }
  }

  @Test
  public void testPermissionsListCourseVariables() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
      .get("/courses/variables");
    assertOk(response, commonPermissions, CommonPermissions.LIST_COURSEBASEVARIABLEKEYS, 200);
  }
  
  @Test
  public void testPermissionsFindCourseVariable() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
    .get("/courses/variables/TV1");
    assertOk(response, commonPermissions, CommonPermissions.FIND_COURSEBASEVARIABLEKEY, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseVariable() throws NoSuchFieldException {
    VariableKey courseVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    String key = courseVariable.getKey();
    
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateVariable)
        .put("/courses/variables/{KEY}", key);
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_COURSEBASEVARIABLEKEY, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/variables/{KEY}", key);
    }
  }
  
  @Test
  public void testPermissionsDeleteCourseVariable() throws NoSuchFieldException {
    VariableKey courseVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    Response deleteResponse = given().headers(getAuthHeaders())
    .delete("/courses/variables/{KEY}", courseVariable.getKey());
    
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_COURSEBASEVARIABLEKEY, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/courses/variables/{KEY}", courseVariable.getKey());
  }
}
