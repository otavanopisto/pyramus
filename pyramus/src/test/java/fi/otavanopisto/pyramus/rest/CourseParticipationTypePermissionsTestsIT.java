package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseParticipationType;
import io.restassured.response.Response;

public class CourseParticipationTypePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseParticipationType(Role role) throws NoSuchFieldException{
    CourseParticipationType courseParticipationType = new CourseParticipationType("Test Type", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");
    
    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSEPARTICIPATIONTYPE, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        deleteParticipationType(id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseParticipationTypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/participationTypes");
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSEPARTICIPATIONTYPES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseParticipationType(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/participationTypes/{ID}", 1);
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSEPARTICIPATIONTYPES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseParticipationType(Role role) throws NoSuchFieldException{
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

    Response updateResponse = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(updateType)
      .put("/courses/participationTypes/{ID}", id);
    assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSEPARTICIPATIONTYPE, 200);
    
    deleteParticipationType(id);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourseParticipationType(Role role) throws NoSuchFieldException{
    CourseParticipationType courseParticipationType = new CourseParticipationType("Delete Type", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");

    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/courses/participationTypes/{ID}", id);    
    
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.ARCHIVE_COURSEPARTICIPATIONTYPE, 204);

    deleteParticipationType(id);
  }

  private void deleteParticipationType(Long id) {
    Response response = given().headers(getAdminAuthHeaders())
      .delete("/courses/participationTypes/{ID}?permanent=true", id);
    assertEquals(204, response.getStatusCode());
  }
}
