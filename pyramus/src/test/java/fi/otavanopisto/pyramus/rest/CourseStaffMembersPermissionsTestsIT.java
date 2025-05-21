package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum;
import io.restassured.response.Response;

public class CourseStaffMembersPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private static final long COURSE_ID = 1000;
  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseStaffMember(Role role) throws NoSuchFieldException{
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSESTAFFMEMBER, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseStaffMembers(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSESTAFFMEMBERS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCourseStaffMember(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
    .get("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, 1l);
    assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSESTAFFMEMBER, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseStaffMemberRole(Role role) throws NoSuchFieldException {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    Long id = null;
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    try {
      id = response.body().jsonPath().getLong("id");
      
      CourseStaffMember updateEntity = new CourseStaffMember(id, null, 1l, CourseStaffMemberRoleEnum.COURSE_TUTOR);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, updateEntity.getId());
      assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSESTAFFMEMBER, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourseStaffMember(Role role) throws NoSuchFieldException {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role)).delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSESTAFFMEMBER, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id);
    
  }
}
