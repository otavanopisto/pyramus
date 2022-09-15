package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseComponent;

@RunWith(Parameterized.class)
public class CourseComponentPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseComponentPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseComponent() throws NoSuchFieldException {
    Long courseId = 1001l;
    
    CourseComponent courseComponent = new CourseComponent(
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseComponent)
      .post("/courses/courses/{COURSEID}/components", courseId);
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSECOMPONENT, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id);
      }
    }
  }
  
  @Test
  public void testPermissionsListCourseComponents() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/1000/components");
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSECOMPONENTS, 200);
  }
  
  @Test
  public void testPermissionsFindCourseComponent() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/1001/components/1003");
    assertOk(response, coursePermissions, CoursePermissions.FIND_COURSECOMPONENT, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseComponent() throws NoSuchFieldException {
    Long courseId = 1001l;
    
    CourseComponent courseComponent = new CourseComponent(
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseComponent)
      .post("/courses/courses/{COURSEID}/components", courseId);
     
    Long id = response.body().jsonPath().getLong("id");
    
    try {
      CourseComponent updateComponent = new CourseComponent(
          id,
          "Updated name", 
          "Updated description",
          132d, 
          1l, 
          Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateComponent)
        .put("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id);
      assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSECOMPONENT, 200);
    } finally {
      given().headers(getAuthHeaders())
        .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id);
    }
  }
  
  @Test
  public void testPermissionsDeleteCourseComponent() throws NoSuchFieldException {
    Long courseId = 1001l;
    
    CourseComponent courseComponent = new CourseComponent(
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseComponent)
      .post("/courses/courses/{COURSEID}/components", courseId);
    
    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id);
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSECOMPONENT, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id);
  }
}
