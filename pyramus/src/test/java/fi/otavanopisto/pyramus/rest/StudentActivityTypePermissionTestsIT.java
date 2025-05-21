package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentActivityTypePermissions;
import fi.otavanopisto.pyramus.rest.model.StudentActivityType;
import io.restassured.response.Response;

public class StudentActivityTypePermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentActivityTypePermissions studentActivityTypePermissions = new StudentActivityTypePermissions();

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentActivityType(Role role) throws NoSuchFieldException {
    StudentActivityType studentActivityType = new StudentActivityType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");

    assertOk(role, response, studentActivityTypePermissions, StudentActivityTypePermissions.CREATE_STUDENTACTIVITYTYPE);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
    
      given().headers(getAdminAuthHeaders())
        .delete("/students/activityTypes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void listStudentActivityTypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/activityTypes");
    
    assertOk(role, response, studentActivityTypePermissions, StudentActivityTypePermissions.LIST_STUDENTACTIVITYTYPES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentActivityType(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/activityTypes/{ID}", 1);
    
    assertOk(role, response, studentActivityTypePermissions, StudentActivityTypePermissions.FIND_STUDENTACTIVITYTYPE);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudentActivityType(Role role) throws NoSuchFieldException {
    StudentActivityType studentActivityType = new StudentActivityType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      StudentActivityType updateStudentActivityType = new StudentActivityType(id, "Updated", Boolean.FALSE);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudentActivityType)
        .put("/students/activityTypes/{ID}", id);
      
      assertOk(role, response, studentActivityTypePermissions, StudentActivityTypePermissions.UPDATE_STUDENTACTIVITYTYPE);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/activityTypes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentActivityType(Role role) throws NoSuchFieldException {
    StudentActivityType studentActivityType = new StudentActivityType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/activityTypes/{ID}", id);
    
    assertOk(role, response, studentActivityTypePermissions, StudentActivityTypePermissions.DELETE_STUDENTACTIVITYTYPE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/students/activityTypes/{ID}?permanent=true", id);
  }
}
