package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentStudyEndReasonPermissions;
import fi.otavanopisto.pyramus.rest.model.StudentStudyEndReason;
import io.restassured.response.Response;

public class StudentStudyEndReasonPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentStudyEndReasonPermissions studentStudyEndReasonPermissions = new StudentStudyEndReasonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudyEndReason(Role role) throws NoSuchFieldException {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "create test", null);
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
    
    assertOk(role, response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.CREATE_STUDENTSTUDYENDREASON);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyEndReasons/{ID}", id);
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudyEndReasons(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studyEndReasons");
    
    assertOk(role, response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.LIST_STUDENTSTUDYENDREASONS);
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudyEndReasons(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studyEndReasons/{ID}", 1);

    assertOk(role, response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.FIND_STUDENTSTUDYENDREASON);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudyEndReason(Role role) throws NoSuchFieldException {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "not updated", null);
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
          
    Long id = response.body().jsonPath().getLong("id");
    try {
      StudentStudyEndReason updateReason = new StudentStudyEndReason(id, "updated", 1l);
      
      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateReason)
        .put("/students/studyEndReasons/{ID}", id);

      assertOk(role, response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.UPDATE_STUDENTSTUDYENDREASON);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyEndReasons/{ID}", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudyEndReason(Role role) throws NoSuchFieldException {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "to be deleted", null);
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
      
    Long id = response.body().jsonPath().getLong("id");

    response = given().headers(getAuthHeaders(role))
      .delete("/students/studyEndReasons/{ID}", id);
    
    assertOk(role, response, studentStudyEndReasonPermissions, StudentStudyEndReasonPermissions.DELETE_STUDENTSTUDYENDREASON, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/studyEndReasons/{ID}?permanent=true", id);
  }
}
