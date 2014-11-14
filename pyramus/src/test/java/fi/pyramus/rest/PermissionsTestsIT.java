package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fi.pyramus.rest.controller.permissions.CalendarPermissions;
import fi.pyramus.rest.model.AcademicTerm;

@RunWith(Parameterized.class)
public class PermissionsTestsIT extends AbstractRESTPermissionsTest {
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static Collection<Object[]> generateData() {
    // The parameter generator returns a List of
    // arrays. Each array has two elements: { role, authcode}.
    return Arrays.asList(new Object[][] {
        { "GUEST" },
        { "USER" },
        { "STUDENT" },
        { "MANAGER" },
        { "ADMINISTRATOR" } });
  }

  public PermissionsTestsIT(String role) {
    setRole(role);
  }
  
  @Test
  public void testPermissionCreateAcadmicTerm() throws NoSuchFieldException {
    DateTime start = getDate(2010, 02, 03);
    DateTime end = getDate(2010, 06, 12);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "create test", start, end, Boolean.FALSE);
    
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.LIST_ACADEMICTERMS);
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
    
    if(roleIsAllowed(getRole(), allowedRolesList)){
      given().headers(getAuthHeaders()).get("/calendar/academicTerms").then().assertThat().statusCode(200);
    }else{
      given().headers(getAuthHeaders()).get("/calendar/academicTerms").then().assertThat().statusCode(403);
    }
  }
  
  @Test
  public void testPermissionsFindAcademicTerms() throws NoSuchFieldException{
    String[] permissions = new CalendarPermissions().getDefaultRoles(fi.pyramus.rest.controller.permissions.CalendarPermissions.LIST_ACADEMICTERMS);
    List<String> allowedRolesList = Arrays.asList(permissions);
    
    if(roleIsAllowed(getRole(), allowedRolesList)){
      given().headers(getAuthHeaders()).get("/calendar/academicTerms/1").then().assertThat().statusCode(200);
    }else{
      given().headers(getAuthHeaders()).get("/calendar/academicTerms/1").then().assertThat().statusCode(403);
    }
  }
  
}
