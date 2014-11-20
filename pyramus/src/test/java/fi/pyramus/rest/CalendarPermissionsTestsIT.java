package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.rest.controller.permissions.CalendarPermissions;
import fi.pyramus.rest.model.AcademicTerm;

@RunWith(Parameterized.class)
public class CalendarPermissionsTestsIT extends AbstractRESTPermissionsTest {
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static List<Object[]> generateData() {
    // The parameter generator returns a List of
    // arrays. Each array has two elements: { role }.
    return Arrays.asList(new Object[][] {
        { "GUEST"},
        { "USER"},
        { "STUDENT"},
        { "MANAGER"},
        { "ADMINISTRATOR"}
      }
    );
  }

  public CalendarPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionCreateAcademicTerm() throws NoSuchFieldException {
    DateTime start = getDate(2010, 02, 03);
    DateTime end = getDate(2010, 06, 12);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "create test", start, end, Boolean.FALSE);
    
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.CREATE_ACADEMICTERM);
    List<String> allowedRolesList = Arrays.asList(permissions);

    if(roleIsAllowed(getRole(), allowedRolesList)){
      given().headers(getAuthHeaders()).contentType("application/json").body(academicTerm).post("/calendar/academicTerms").then().assertThat().statusCode(200);
    }else{
      given().headers(getAuthHeaders()).contentType("application/json").body(academicTerm).post("/calendar/academicTerms").then().assertThat().statusCode(403);
    }
  }

  @Test
  public void testPermissionsListAcademicTerms() throws NoSuchFieldException {
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.LIST_ACADEMICTERMS);
    List<String> allowedRolesList = Arrays.asList(permissions);
    
    assertOk("/calendar/academicTerms", allowedRolesList);
  }
  
  @Test
  public void testPermissionsFindAcademicTerm() throws NoSuchFieldException{
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.FIND_ACADEMICTERM);
    List<String> allowedRolesList = Arrays.asList(permissions);
    
    assertOk("/calendar/academicTerms/1", allowedRolesList); 
  }
  
  @Test
  public void testPermissionsUpdateAcademicTerm() throws NoSuchFieldException{
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.UPDATE_ACADEMICTERM);
    List<String> allowedRolesList = Arrays.asList(permissions);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "not updated", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);

    try {
      response.then().body("id", not(is((Long) null))).body("name", is(academicTerm.getName())).body("startDate", is(academicTerm.getStartDate().toString()))
          .body("endDate", is(academicTerm.getEndDate().toString())).body("archived", is(academicTerm.getArchived()));

      AcademicTerm updateAcademicTerm = new AcademicTerm(id, "updated", getDate(2010, 03, 04), getDate(2010, 07, 13), Boolean.FALSE);
      
      if(roleIsAllowed(getRole(), allowedRolesList)){
        given().headers(getAuthHeaders()).contentType("application/json").body(updateAcademicTerm)
        .put("/calendar/academicTerms/{ID}", id).then().assertThat().statusCode(200);
      }else{
        given().headers(getAuthHeaders()).contentType("application/json").body(updateAcademicTerm)
        .put("/calendar/academicTerms/{ID}", id).then().assertThat().statusCode(403);
      }

    } finally {
      given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id).then().statusCode(204);
    }
  }

  @Test
  public void testPermissionsDeleteAcademicTerm() throws NoSuchFieldException{
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.DELETE_ACADEMICTERM);
    List<String> allowedRolesList = Arrays.asList(permissions);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "to be deleted", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);

    given().headers(getAdminAuthHeaders()).get("/calendar/academicTerms/{ID}", id).then().statusCode(200);
    
    if(roleIsAllowed(getRole(), allowedRolesList)){
      given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}", id).then().statusCode(204);
    }else{
      given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}", id).then().statusCode(403);
    }
  }
  
  @Test
  public void testPermissionsCoursesByTerm() throws NoSuchFieldException {
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.FIND_COURSESBYACADEMICTERM);
    List<String> allowedRolesList = Arrays.asList(permissions);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "2010 spring", getDate(2010, 01, 01), getDate(2010, 06, 1), Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);

    if(roleIsAllowed(getRole(), allowedRolesList)){
      given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}/courses", id).then().statusCode(200);
    }else{
      given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}/courses", id).then().statusCode(403);
    }
    
    given().headers(getAdminAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id).then().statusCode(204);
  }
}
