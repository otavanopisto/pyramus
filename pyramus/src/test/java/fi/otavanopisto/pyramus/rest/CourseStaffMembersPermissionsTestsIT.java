package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;

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
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, 1l);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSESTAFFMEMBER, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
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
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, 1l);
    Long id = null;
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    try {
      id = response.body().jsonPath().getLong("id");
      
      CourseStaffMember updateEntity = new CourseStaffMember(id, null, 1l, 2l);
      
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
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, 1l);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders()).delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSESTAFFMEMBER, 204);
    
    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.toString().equals("204"))
      given().headers(getAdminAuthHeaders()).delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    
  }
}
