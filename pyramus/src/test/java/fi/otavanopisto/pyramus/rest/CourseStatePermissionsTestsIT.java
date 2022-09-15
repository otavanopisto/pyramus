package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseState;

@RunWith(Parameterized.class)
public class CourseStatePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  
  public CourseStatePermissionsTestsIT(String role){
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseState() throws NoSuchFieldException{
    CourseState courseState = new CourseState("Test State", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSESTATE, 200);
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/courseStates/{ID}?permanent=true", id);
      }
    }   
  }
  
  @Test
  public void testPermissionsListCourseStates() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courseStates");
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSESTATES, 200);
  }
  
  @Test
  public void testPermissionsFindCourseState() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courseStates/{ID}", 3);
    assertOk(response, coursePermissions, CoursePermissions.FIND_COURSESTATE, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseState() throws NoSuchFieldException {
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

    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateState)
      .put("/courses/courseStates/{ID}", id);
    
    assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSESTATE, 200);  

    given().headers(getAdminAuthHeaders())
      .delete("/courses/courseStates/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testPermissionsArchiveCourseState() throws NoSuchFieldException {
    CourseState courseState = new CourseState("Delete Test", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");

    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/courseStates/{ID}", id);
    
    assertOk(deleteResponse, coursePermissions, CoursePermissions.ARCHIVE_COURSESTATE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/courseStates/{ID}?permanent=true", id);
  }
}
