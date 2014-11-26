package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.CoursePermissions;
import fi.pyramus.rest.model.CourseParticipationType;

@RunWith(Parameterized.class)
public class CourseParticipationTypePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseParticipationTypePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseParticipationType() throws NoSuchFieldException{
    CourseParticipationType courseParticipationType = new CourseParticipationType("Test Type", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSEPARTICIPATIONTYPE, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.equals(200)){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/participationTypes/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListCourseParticipationTypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/participationTypes");
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSEPARTICIPATIONTYPES, 200);
  }
  
  @Test
  public void testPermissionsListCourseParticipationType() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/participationTypes/{ID}", 1);
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSEPARTICIPATIONTYPES, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseParticipationType() throws NoSuchFieldException{
    CourseParticipationType courseParticipationType = new CourseParticipationType(
        "Update test", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    CourseParticipationType updateType = new CourseParticipationType(
        id,
        "Updated name",
        Boolean.FALSE);

    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateType)
      .put("/courses/participationTypes/{ID}", id);
    assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSEPARTICIPATIONTYPE, 200);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/participationTypes/{ID}?permanent=true", id);
  }
  
  @Test
  public void testPermissionsDeleteCourseParticipationType() throws NoSuchFieldException{
    CourseParticipationType courseParticipationType = new CourseParticipationType("Delete Type", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/participationTypes/{ID}", id);    
    
    assertOk(deleteResponse, coursePermissions, CoursePermissions.ARCHIVE_COURSEPARTICIPATIONTYPE, 204);

    given().headers(getAuthHeaders())
      .delete("/courses/participationTypes/{ID}?permanent=true", id);
  }
}
