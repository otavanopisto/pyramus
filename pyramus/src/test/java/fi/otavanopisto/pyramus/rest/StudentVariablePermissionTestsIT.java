package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.VariableKey;
import fi.otavanopisto.pyramus.rest.model.VariableType;
import io.restassured.response.Response;

public class StudentVariablePermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private UserPermissions userPermissions = new UserPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentVariables(Role role) throws NoSuchFieldException {
    VariableKey studentVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");

    assertOk(role, response, userPermissions, UserPermissions.CREATE_USERVARIABLE);

    given().headers(getAdminAuthHeaders())
      .delete("/students/variables/{KEY}", studentVariable.getKey());
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentVariables(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/variables");
    
    assertOk(role, response, userPermissions, UserPermissions.LIST_USERVARIABLES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentVariable(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
    .get("/students/variables/TV1");

    assertOk(role, response, userPermissions, UserPermissions.FIND_USERVARIABLE);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudentVariable(Role role) throws NoSuchFieldException {
    VariableKey studentVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");
        
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateVariable)
        .put("/students/variables/{KEY}", updateVariable.getKey());

      assertOk(role, response, userPermissions, UserPermissions.UPDATE_USERVARIABLE);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/variables/{KEY}", studentVariable.getKey());
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentVariable(Role role) throws NoSuchFieldException {
    VariableKey studentVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/variables/{KEY}", studentVariable.getKey());
    
    assertOk(role, response, userPermissions, UserPermissions.DELETE_USERVARIABLE, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/variables/{KEY}", studentVariable.getKey());
    }
  }
}
