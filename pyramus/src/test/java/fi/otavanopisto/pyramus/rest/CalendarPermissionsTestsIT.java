package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fi.otavanopisto.pyramus.rest.controller.permissions.CalendarPermissions;
import fi.otavanopisto.pyramus.rest.model.AcademicTerm;
import io.restassured.response.Response;

@RunWith(Parameterized.class)
public class CalendarPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CalendarPermissions calendarPermissions = new CalendarPermissions();

  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }

  public CalendarPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateAcademicTerm() throws NoSuchFieldException {
    OffsetDateTime start = getDate(2010, 02, 03);
    OffsetDateTime end = getDate(2010, 06, 12);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "create test", start, end, Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(academicTerm)
        .post("/calendar/academicTerms");
    
    assertOk(response, calendarPermissions, CalendarPermissions.CREATE_ACADEMICTERM, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
          .delete("/calendar/academicTerms/{ID}?permanent=true", id);
      }
    }    
  }

  @Test
  public void testPermissionsListAcademicTerms() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
        .get("/calendar/academicTerms"), calendarPermissions, CalendarPermissions.LIST_ACADEMICTERMS);
  }
  
  @Test
  public void testPermissionsFindAcademicTerm() throws NoSuchFieldException{
    assertOk(given().headers(getAuthHeaders())
        .get("/calendar/academicTerms/{ID}", 1), calendarPermissions, CalendarPermissions.FIND_ACADEMICTERM);
  }
  
  @Test
  public void testPermissionsUpdateAcademicTerm() throws NoSuchFieldException{
    AcademicTerm academicTerm = new AcademicTerm(null, "not updated", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));

    try {
      response.then().body("id", not(is((Long) null))).body("name", is(academicTerm.getName())).body("startDate", is(academicTerm.getStartDate().toString()))
          .body("endDate", is(academicTerm.getEndDate().toString())).body("archived", is(academicTerm.getArchived()));

      AcademicTerm updateAcademicTerm = new AcademicTerm(id, "updated", getDate(2010, 03, 04), getDate(2010, 07, 13), Boolean.FALSE);
      
      Response updateResponse = given().headers(getAuthHeaders())
          .contentType("application/json")
          .body(updateAcademicTerm)
          .put("/calendar/academicTerms/{ID}", id);
      assertOk(updateResponse, calendarPermissions, CalendarPermissions.UPDATE_ACADEMICTERM, 200);
    } finally {
      given().headers(getAdminAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id);
    }
  }

  @Test
  public void testPermissionsDeleteAcademicTerm() throws NoSuchFieldException{
    AcademicTerm academicTerm = new AcademicTerm(null, "to be deleted", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    Response deleteResponse = given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}", id);    
    assertOk(deleteResponse, calendarPermissions, CalendarPermissions.DELETE_ACADEMICTERM, 204);
    given().headers(getAdminAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id);
  }
  
  @Test
  public void testPermissionsCoursesByTerm() throws NoSuchFieldException {  
    AcademicTerm academicTerm = new AcademicTerm(null, "2010 spring", getDate(2010, 01, 01), getDate(2010, 06, 1), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));

    Response courseByTermResponse = given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}/courses", id);
    assertOk(courseByTermResponse, calendarPermissions, CalendarPermissions.FIND_COURSESBYACADEMICTERM, 200);
    
    given().headers(getAdminAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id);
  }
}
