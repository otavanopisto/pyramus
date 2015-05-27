package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.CoursePermissions;
import fi.pyramus.rest.model.CourseOptionality;
import fi.pyramus.rest.model.CourseStudent;

@RunWith(Parameterized.class)
public class CourseStudentsPermissionsTestsIT extends AbstractRESTPermissionsTest {
  
  private static final long COURSE_ID = 1000;
  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
    
  public CourseStudentsPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseStudent() throws NoSuchFieldException {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, 3l, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSESTUDENT, 200);
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
          .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id);
      }
    }
  }

  @Test
  public void testPermissionsListCourseStudents() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/students", COURSE_ID);
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSESTUDENTS, 200);
  }
  
  @Test
  public void testPermissionsFindCourseStudent() throws NoSuchFieldException  {
    Response response = given().headers(getAuthHeaders())
    .get("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, 5l);
    assertOk(response, coursePermissions, CoursePermissions.FIND_COURSESTUDENT, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseStudent() throws NoSuchFieldException  {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, 3l, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);

    Long id = response.body().jsonPath().getLong("id");

    CourseStudent updateEntity = new CourseStudent(id, COURSE_ID, 3l, getDate(2012, 5, 6), false, 2l, 2l, true, CourseOptionality.OPTIONAL, null);
    
    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateEntity)
      .put("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, updateEntity.getId());
    assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSESTUDENT, 200);

    given().headers(getAdminAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", COURSE_ID, id);
  }
  
  @Test
  public void testPermissionsDeleteCourseStudent() throws NoSuchFieldException  {
    CourseStudent entity = new CourseStudent(null, COURSE_ID, 3l, getDate(2014, 5, 6), false, 1l, 1l, false, CourseOptionality.MANDATORY, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/students", COURSE_ID);
    
    int id = response.body().jsonPath().getInt("id");
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id);
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSESTUDENT, 204);

    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.toString().equals("204"))
      given().headers(getAdminAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}", COURSE_ID, id);
  }
}
