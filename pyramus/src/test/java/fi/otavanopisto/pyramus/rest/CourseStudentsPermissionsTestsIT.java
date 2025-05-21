package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.CourseOptionality;
import fi.otavanopisto.pyramus.rest.model.CourseStudent;
import io.restassured.response.Response;

public class CourseStudentsPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private static final long COURSE_ID = 1000;
  private static final long STUDENT_ID = 13;
  private CoursePermissions coursePermissions = new CoursePermissions();
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseStudent(Role role) throws NoSuchFieldException {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, STUDENT_ID, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSESTUDENT, 200);

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

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateOwnCourseStudent(Role role) throws NoSuchFieldException {
    if ("STUDENT".equals(role)) {
      Long userId = getUserIdForRole(role);
      CourseStudent entity = new CourseStudent(null, COURSE_ID, userId, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
      
      Response response = given().headers(getAuthHeaders(role))
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

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseStudents(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courses/{COURSEID}/students", COURSE_ID);
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSESTUDENTS, 200);
  }
  
  /**
   * Test case where study guider cannot find (is not in the same groups) the student
   */
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCourseStudent(Role role) throws NoSuchFieldException  {
    Response response = given().headers(getAuthHeaders(role))
        .get("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, 5l);

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSESTUDENT, 403);
    } else {
      assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSESTUDENT, 200);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseStudent(Role role) throws NoSuchFieldException  {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, STUDENT_ID, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);

    Long id = response.body().jsonPath().getLong("id");

    CourseStudent updateEntity = new CourseStudent(id, COURSE_ID, STUDENT_ID, getDate(2012, 5, 6), false, 2l, 2l, true, CourseOptionality.OPTIONAL, null);
    
    try {
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, updateEntity.getId());

      assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSESTUDENT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id)
        .then()
        .statusCode(204);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateOwnCourseStudent(Role role) throws NoSuchFieldException  {
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
        Response updateResponse = given().headers(getAuthHeaders(role))
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
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourseStudent(Role role) throws NoSuchFieldException  {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, STUDENT_ID, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    int id = response.body().jsonPath().getInt("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id);
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSESTUDENT, 204);

    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id)
        .then()
        .statusCode(204);      
  }
}
