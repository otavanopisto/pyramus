package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.UserPermissions;
import fi.pyramus.rest.model.VariableKey;
import fi.pyramus.rest.model.VariableType;

@RunWith(Parameterized.class)
public class StudentVariablePermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentVariablePermissionTestsIT(String role) {
    this.role = role;
  }
  
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }

  private UserPermissions userPermissions = new UserPermissions();
  
  @Test
  public void testCreateStudentVariables() throws NoSuchFieldException {
    VariableKey studentVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");

    assertOk(response, userPermissions, UserPermissions.CREATE_USERVARIABLE);

    given().headers(getAdminAuthHeaders())
      .delete("/students/variables/{KEY}", studentVariable.getKey());
  }

  @Test
  public void testListStudentVariables() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/variables");
    
    assertOk(response, userPermissions, UserPermissions.LIST_USERVARIABLES);
  }
  
  @Test
  public void testFindStudentVariable() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
    .get("/students/variables/TV1");

    assertOk(response, userPermissions, UserPermissions.FIND_USERVARIABLE);
  }
  
  @Test
  public void testUpdateStudentVariable() throws NoSuchFieldException {
    VariableKey studentVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");
        
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateVariable)
        .put("/students/variables/{KEY}", updateVariable.getKey());

      assertOk(response, userPermissions, UserPermissions.UPDATE_USERVARIABLE);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/variables/{KEY}", studentVariable.getKey());
    }
  }
  
  @Test
  public void testDeleteStudentVariable() throws NoSuchFieldException {
    VariableKey studentVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");
    
    response = given().headers(getAuthHeaders())
      .delete("/students/variables/{KEY}", studentVariable.getKey());
    
    assertOk(response, userPermissions, UserPermissions.DELETE_USERVARIABLE, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/variables/{KEY}", studentVariable.getKey());
    }
  }
}
