package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentStudyEndReasonPermissions;
import fi.otavanopisto.pyramus.rest.model.StudentStudyEndReason;

@RunWith(Parameterized.class)
public class StudentStudyEndReasonPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentStudyEndReasonPermissionTestsIT(String role) {
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
  
  private StudentStudyEndReasonPermissions studentStudyEndReasonPermissions = new StudentStudyEndReasonPermissions();
  
  @Test
  public void testCreateStudyEndReason() throws NoSuchFieldException {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "create test", null);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
    
    assertOk(response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.CREATE_STUDENTSTUDYENDREASON);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyEndReasons/{ID}", id);
    }
  }

  @Test
  public void testListStudyEndReasons() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studyEndReasons");
    
    assertOk(response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.LIST_STUDENTSTUDYENDREASONS);
  }

  @Test
  public void testFindStudyEndReasons() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studyEndReasons/{ID}", 1);

    assertOk(response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.FIND_STUDENTSTUDYENDREASON);
  }
  
  @Test
  public void testUpdateStudyEndReason() throws NoSuchFieldException {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "not updated", null);
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
          
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentStudyEndReason updateReason = new StudentStudyEndReason(id, "updated", 1l);
      
      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateReason)
        .put("/students/studyEndReasons/{ID}", id);

      assertOk(response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.UPDATE_STUDENTSTUDYENDREASON);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyEndReasons/{ID}", id);
    }
  }
  
  @Test
  public void testDeleteStudyEndReason() throws NoSuchFieldException {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "to be deleted", null);
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
      
    Long id = new Long(response.body().jsonPath().getInt("id"));

    response = given().headers(getAuthHeaders())
      .delete("/students/studyEndReasons/{ID}", id);
    
    assertOk(response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.DELETE_STUDENTSTUDYENDREASON, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/studyEndReasons/{ID}?permanent=true", id);
  }
}
