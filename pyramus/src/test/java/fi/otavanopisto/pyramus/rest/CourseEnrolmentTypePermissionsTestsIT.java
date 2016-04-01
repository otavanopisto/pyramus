package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseEnrolmentType;

@RunWith(Parameterized.class)
public class CourseEnrolmentTypePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseEnrolmentTypePermissionsTestsIT(String role){
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseEnrolmentType() throws NoSuchFieldException {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Test Enrolment Type");

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");

    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSEENROLMENTTYPE, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/enrolmentTypes/{ID}", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListCourseEnrolmentTypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/enrolmentTypes");
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSEENROLMENTTYPES, 200);
  }
  
  @Test
  public void testPermissionsFindCourseEnrolmentType() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/enrolmentTypes/{ID}", 1);
    assertOk(response, coursePermissions, CoursePermissions.FIND_COURSEENROLMENTTYPE, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseState() throws NoSuchFieldException {
    CourseEnrolmentType enrolmentType = new CourseEnrolmentType("Update test");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(enrolmentType)
      .post("/courses/enrolmentTypes");

    Long id = new Long(response.body().jsonPath().getInt("id"));

    CourseEnrolmentType updateEnrolmentType = new CourseEnrolmentType(id, "Updated name");

    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateEnrolmentType)
      .put("/courses/enrolmentTypes/{ID}", id);
    
    assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSEENROLMENTTYPE, 200);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/enrolmentTypes/{ID}", id);
  }
  
  @Test
  public void testPermissionsDeleteCourseEnrolmentType() throws NoSuchFieldException {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Delete Test");

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");
      
    Long id = new Long(response.body().jsonPath().getInt("id"));

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/enrolmentTypes/{ID}", id);
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSEENROLMENTTYPE, 204);
    
    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.toString().equals("204")){
      given().headers(getAdminAuthHeaders())
      .delete("/courses/enrolmentTypes/{ID}", id);
    }   
  }
}
