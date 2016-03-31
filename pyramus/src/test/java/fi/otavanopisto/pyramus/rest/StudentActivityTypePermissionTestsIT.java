package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentActivityTypePermissions;
import fi.otavanopisto.pyramus.rest.model.StudentActivityType;

@RunWith(Parameterized.class)
public class StudentActivityTypePermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentActivityTypePermissionTestsIT(String role) {
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
  
  private StudentActivityTypePermissions studentActivityTypePermissions = new StudentActivityTypePermissions();

  @Test
  public void testCreateStudentActivityType() throws NoSuchFieldException {
    StudentActivityType studentActivityType = new StudentActivityType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");

    assertOk(response, studentActivityTypePermissions, StudentActivityTypePermissions.CREATE_STUDENTACTIVITYTYPE);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
    
      given().headers(getAdminAuthHeaders())
        .delete("/students/activityTypes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void listStudentActivityTypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/activityTypes");
    
    assertOk(response, studentActivityTypePermissions, StudentActivityTypePermissions.LIST_STUDENTACTIVITYTYPES);
  }
  
  @Test
  public void testFindStudentActivityType() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/activityTypes/{ID}", 1);
    
    assertOk(response, studentActivityTypePermissions, StudentActivityTypePermissions.FIND_STUDENTACTIVITYTYPE);
  }
  
  @Test
  public void testUpdateStudentActivityType() throws NoSuchFieldException {
    StudentActivityType studentActivityType = new StudentActivityType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentActivityType updateStudentActivityType = new StudentActivityType(id, "Updated", Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudentActivityType)
        .put("/students/activityTypes/{ID}", id);
      
      assertOk(response, studentActivityTypePermissions, StudentActivityTypePermissions.UPDATE_STUDENTACTIVITYTYPE);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/activityTypes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteStudentActivityType() throws NoSuchFieldException {
    StudentActivityType studentActivityType = new StudentActivityType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/activityTypes/{ID}", id);
    
    assertOk(response, studentActivityTypePermissions, StudentActivityTypePermissions.DELETE_STUDENTACTIVITYTYPE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/students/activityTypes/{ID}?permanent=true", id);
  }
}
