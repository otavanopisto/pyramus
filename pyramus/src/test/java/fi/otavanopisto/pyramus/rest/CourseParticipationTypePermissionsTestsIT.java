package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseParticipationType;

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

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        deleteParticipationType(id);
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
     
    Long id = response.body().jsonPath().getLong("id");
    CourseParticipationType updateType = new CourseParticipationType(
        id,
        "Updated name",
        Boolean.FALSE);

    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateType)
      .put("/courses/participationTypes/{ID}", id);
    assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSEPARTICIPATIONTYPE, 200);
    
    deleteParticipationType(id);
  }
  
  @Test
  public void testPermissionsDeleteCourseParticipationType() throws NoSuchFieldException{
    CourseParticipationType courseParticipationType = new CourseParticipationType("Delete Type", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");

    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/participationTypes/{ID}", id);    
    
    assertOk(deleteResponse, coursePermissions, CoursePermissions.ARCHIVE_COURSEPARTICIPATIONTYPE, 204);

    deleteParticipationType(id);
  }

  private void deleteParticipationType(Long id) {
    Response response = given().headers(getAdminAuthHeaders())
      .delete("/courses/participationTypes/{ID}?permanent=true", id);
    assertEquals(204, response.getStatusCode());
  }
}
