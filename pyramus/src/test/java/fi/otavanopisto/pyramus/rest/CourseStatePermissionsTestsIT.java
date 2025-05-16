package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseState;
import io.restassured.response.Response;

public class CourseStatePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseState(Role role) throws NoSuchFieldException{
    CourseState courseState = new CourseState("Test State", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");
    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSESTATE, 200);
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/courseStates/{ID}?permanent=true", id);
      }
    }   
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseStates(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courseStates");
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSESTATES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCourseState(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courseStates/{ID}", 3);
    assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSESTATE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseState(Role role) throws NoSuchFieldException {
    CourseState courseState = new CourseState(
        "Update test", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");
     
    Long id = response.body().jsonPath().getLong("id");

    CourseState updateState = new CourseState(
      id,
      "Updated name",
      Boolean.FALSE);

    Response updateResponse = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(updateState)
      .put("/courses/courseStates/{ID}", id);
    
    assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSESTATE, 200);  

    given().headers(getAdminAuthHeaders())
      .delete("/courses/courseStates/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsArchiveCourseState(Role role) throws NoSuchFieldException {
    CourseState courseState = new CourseState("Delete Test", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");

    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/courses/courseStates/{ID}", id);
    
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.ARCHIVE_COURSESTATE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/courseStates/{ID}?permanent=true", id);
  }
}
