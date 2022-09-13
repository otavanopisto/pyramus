package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRole;

@RunWith(Parameterized.class)
public class CourseStaffMemberRolesPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseStaffMemberRolesPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseStaffMemberRole() throws NoSuchFieldException {
    CourseStaffMemberRole entity = new CourseStaffMemberRole(null, "created");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/staffMemberRoles");
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_STAFFMEMBERROLE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/staffMemberRoles/{ID}", id);
      }
    }
  }

  @Test
  public void testPermissionsListCourseStaffMemberRoles() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
      .get("/courses/staffMemberRoles");
    assertOk(response, coursePermissions, CoursePermissions.LIST_STAFFMEMBERROLES, 200);
  }
  
  @Test
  public void testPermissionsFindCourseStaffMemberRole() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
    .get("/courses/staffMemberRoles/{ID}", 1l);
    assertOk(response, coursePermissions, CoursePermissions.FIND_STAFFMEMBERROLE, 200);
  }
  
  @Test
  public void testUpdateCourseStaffMemberRole() throws NoSuchFieldException {
    CourseStaffMemberRole entity = new CourseStaffMemberRole(null, "not updated");
    Long id = null;
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/staffMemberRoles");
    
    try {
      id = response.body().jsonPath().getLong("id");
      
      CourseStaffMemberRole updateEntity = new CourseStaffMemberRole(id, "updated");
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/staffMemberRoles/{ID}", updateEntity.getId());
      
      assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_STAFFMEMBERROLE, 200);


    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/staffMemberRoles/{ID}", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteCourseStaffMemberRole() throws NoSuchFieldException {
    CourseStaffMemberRole entity = new CourseStaffMemberRole(null, "to be deleted");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/staffMemberRoles");

    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/staffMemberRoles/{ID}", id);
    
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_STAFFMEMBERROLE, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/courses/staffMemberRoles/{ID}", id);
  }
}
