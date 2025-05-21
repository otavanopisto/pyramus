package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.time.OffsetDateTime;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CalendarPermissions;
import fi.otavanopisto.pyramus.rest.model.AcademicTerm;
import io.restassured.response.Response;

public class CalendarPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CalendarPermissions calendarPermissions = new CalendarPermissions();

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateAcademicTerm(Role role) throws NoSuchFieldException {
    OffsetDateTime start = getDate(2010, 02, 03);
    OffsetDateTime end = getDate(2010, 06, 12);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "create test", start, end, Boolean.FALSE);

    Response response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(academicTerm)
        .post("/calendar/academicTerms");
    
    assertOk(role, response, calendarPermissions, CalendarPermissions.CREATE_ACADEMICTERM, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/calendar/academicTerms/{ID}?permanent=true", id);
      }
    }    
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListAcademicTerms(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
        .get("/calendar/academicTerms"), calendarPermissions, CalendarPermissions.LIST_ACADEMICTERMS);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindAcademicTerm(Role role) throws NoSuchFieldException{
    assertOk(role, given().headers(getAuthHeaders(role))
        .get("/calendar/academicTerms/{ID}", 1), calendarPermissions, CalendarPermissions.FIND_ACADEMICTERM);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateAcademicTerm(Role role) throws NoSuchFieldException{
    AcademicTerm academicTerm = new AcademicTerm(null, "not updated", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = response.body().jsonPath().getLong("id");

    try {
      AcademicTerm updateAcademicTerm = new AcademicTerm(id, "updated", getDate(2010, 03, 04), getDate(2010, 07, 13), Boolean.FALSE);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
          .contentType("application/json")
          .body(updateAcademicTerm)
          .put("/calendar/academicTerms/{ID}", id);
      assertOk(role, updateResponse, calendarPermissions, CalendarPermissions.UPDATE_ACADEMICTERM, 200);
    } finally {
      given().headers(getAdminAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id);
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteAcademicTerm(Role role) throws NoSuchFieldException{
    AcademicTerm academicTerm = new AcademicTerm(null, "to be deleted", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role)).delete("/calendar/academicTerms/{ID}", id);    
    assertOk(role, deleteResponse, calendarPermissions, CalendarPermissions.DELETE_ACADEMICTERM, 204);
    given().headers(getAdminAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCoursesByTerm(Role role) throws NoSuchFieldException {  
    AcademicTerm academicTerm = new AcademicTerm(null, "2010 spring", getDate(2010, 01, 01), getDate(2010, 06, 1), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = response.body().jsonPath().getLong("id");

    Response courseByTermResponse = given().headers(getAuthHeaders(role)).get("/calendar/academicTerms/{ID}/courses", id);
    assertOk(role, courseByTermResponse, calendarPermissions, CalendarPermissions.FIND_COURSESBYACADEMICTERM, 200);
    
    given().headers(getAdminAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id);
  }
}
