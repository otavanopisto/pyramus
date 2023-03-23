package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum;

@RunWith(Parameterized.class)
public class CourseStaffMembersPermissionsTestsIT extends AbstractRESTPermissionsTest {
  
  private static final long COURSE_ID = 1000;
  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseStaffMembersPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseStaffMember() throws NoSuchFieldException{
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSESTAFFMEMBER, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
      }
    }
  }

  @Test
  public void testPermissionsListCourseStaffMembers() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSESTAFFMEMBERS, 200);
  }
  
  @Test
  public void testPermissionsFindCourseStaffMember() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
    .get("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, 1l);
    assertOk(response, coursePermissions, CoursePermissions.FIND_COURSESTAFFMEMBER, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseStaffMemberRole() throws NoSuchFieldException {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    Long id = null;
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    try {
      id = response.body().jsonPath().getLong("id");
      
      CourseStaffMember updateEntity = new CourseStaffMember(id, null, 1l, CourseStaffMemberRoleEnum.COURSE_TUTOR);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, updateEntity.getId());
      assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSESTAFFMEMBER, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    }
  }
  
  @Test
  public void testPermissionsDeleteCourseStaffMember() throws NoSuchFieldException {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders()).delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSESTAFFMEMBER, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    
  }
}
