package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseEnrolmentType;
import io.restassured.response.Response;

public class CourseEnrolmentTypePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseEnrolmentType(Role role) throws NoSuchFieldException {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Test Enrolment Type");

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");

    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSEENROLMENTTYPE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/enrolmentTypes/{ID}", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseEnrolmentTypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/enrolmentTypes");
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSEENROLMENTTYPES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCourseEnrolmentType(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/enrolmentTypes/{ID}", 1);
    assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSEENROLMENTTYPE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseState(Role role) throws NoSuchFieldException {
    CourseEnrolmentType enrolmentType = new CourseEnrolmentType("Update test");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(enrolmentType)
      .post("/courses/enrolmentTypes");

    Long id = response.body().jsonPath().getLong("id");

    CourseEnrolmentType updateEnrolmentType = new CourseEnrolmentType(id, "Updated name");

    Response updateResponse = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(updateEnrolmentType)
      .put("/courses/enrolmentTypes/{ID}", id);
    
    assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSEENROLMENTTYPE, 200);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/enrolmentTypes/{ID}", id);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourseEnrolmentType(Role role) throws NoSuchFieldException {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Delete Test");

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");
      
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/courses/enrolmentTypes/{ID}", id);
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSEENROLMENTTYPE, 204);
    
    if (deleteResponse.statusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/enrolmentTypes/{ID}", id);
    }   
  }
}
