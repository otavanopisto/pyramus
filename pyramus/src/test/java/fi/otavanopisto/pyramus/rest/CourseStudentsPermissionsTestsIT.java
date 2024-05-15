package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.CourseOptionality;
import fi.otavanopisto.pyramus.rest.model.CourseStudent;

@RunWith(Parameterized.class)
public class CourseStudentsPermissionsTestsIT extends AbstractRESTPermissionsTest {
  
  private static final long COURSE_ID = 1000;
  private static final long STUDENT_ID = 13;
  private CoursePermissions coursePermissions = new CoursePermissions();
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
    
  public CourseStudentsPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseStudent() throws NoSuchFieldException {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, STUDENT_ID, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSESTUDENT, 200);

    if (response.getStatusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
          .then()
          .statusCode(204);
      }
    }
  }

  @Test
  public void testPermissionsCreateOwnCourseStudent() throws NoSuchFieldException {
    if ("STUDENT".equals(role)) {
      Long userId = getUserIdForRole(role);
      CourseStudent entity = new CourseStudent(null, COURSE_ID, userId, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
      
      Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(entity)
        .post("/courses/courses/{COURSEID}/students", COURSE_ID);
      
      response.then().statusCode(200);
  
      if (response.getStatusCode() == 200) {
        Long id = response.body().jsonPath().getLong("id");
        if (id != null) {
          given().headers(getAdminAuthHeaders())
            .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
            .then()
            .statusCode(204);
        }
      }
    }
  }

  @Test
  public void testPermissionsListCourseStudents() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/students", COURSE_ID);
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSESTUDENTS, 200);
  }
  
  /**
   * Test case where study guider cannot find (is not in the same groups) the student
   */
  @Test
  public void testPermissionsFindCourseStudent() throws NoSuchFieldException  {
    Response response = given().headers(getAuthHeaders())
        .get("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, 5l);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(response, coursePermissions, CoursePermissions.FIND_COURSESTUDENT, 403);
    } else {
      assertOk(response, coursePermissions, CoursePermissions.FIND_COURSESTUDENT, 200);
    }
  }
  
  @Test
  public void testPermissionsUpdateCourseStudent() throws NoSuchFieldException  {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, STUDENT_ID, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);

    Long id = response.body().jsonPath().getLong("id");

    CourseStudent updateEntity = new CourseStudent(id, COURSE_ID, STUDENT_ID, getDate(2012, 5, 6), false, 2l, 2l, true, CourseOptionality.OPTIONAL, null);
    
    try {
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, updateEntity.getId());

      assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSESTUDENT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testPermissionsUpdateOwnCourseStudent() throws NoSuchFieldException  {
    if ("STUDENT".equals(role)) {
      Long userId = getUserIdForRole(role);
      CourseStudent entity = new CourseStudent(null, COURSE_ID, userId, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
      
      Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(entity)
        .post("/courses/courses/{COURSEID}/students", COURSE_ID);
  
      Long id = response.body().jsonPath().getLong("id");
  
      CourseStudent updateEntity = new CourseStudent(id, COURSE_ID, userId, getDate(2012, 5, 6), false, 2l, 2l, true, CourseOptionality.OPTIONAL, null);
      
      try {
        Response updateResponse = given().headers(getAuthHeaders())
          .contentType("application/json")
          .body(updateEntity)
          .put("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, updateEntity.getId());
  
        updateResponse.then().statusCode(200);
      } finally {
        given().headers(getAdminAuthHeaders())
          .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
          .then()
          .statusCode(204);
      }
    }
  }
  
  @Test
  public void testPermissionsDeleteCourseStudent() throws NoSuchFieldException  {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, STUDENT_ID, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    int id = response.body().jsonPath().getInt("id");
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id);
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSESTUDENT, 204);

    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id)
        .then()
        .statusCode(204);      
  }
}
