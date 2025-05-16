package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseComponent;
import io.restassured.response.Response;

public class CourseComponentPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseComponent(Role role) throws NoSuchFieldException {
    Long courseId = 1001l;
    
    CourseComponent courseComponent = new CourseComponent(
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(courseComponent)
      .post("/courses/courses/{COURSEID}/components", courseId);
    
    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSECOMPONENT, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseComponents(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courses/1000/components");
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSECOMPONENTS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCourseComponent(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courses/1001/components/1003");
    assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSECOMPONENT, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseComponent(Role role) throws NoSuchFieldException {
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

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateComponent)
        .put("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id);
      assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSECOMPONENT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourseComponent(Role role) throws NoSuchFieldException {
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
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id);
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSECOMPONENT, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id);
  }
}
