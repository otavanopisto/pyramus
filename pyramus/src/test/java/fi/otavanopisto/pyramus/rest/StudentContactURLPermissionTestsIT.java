package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactURL;

@RunWith(Parameterized.class)
public class StudentContactURLPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentContactURLPermissionTestsIT(String role) {
    this.role = role;
  }
  
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  private final static long TEST_STUDENT_ID = 3l;

  @Test
  public void testCreateStudentContactURL() throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/students/students/{ID}/contactURLs", TEST_STUDENT_ID);

    assertOk(response, studentPermissions, StudentPermissions.CREATE_STUDENTCONTACTURL);

    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @Test
  public void testListStudentContactURLs() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/contactURLs", TEST_STUDENT_ID);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTCONTACTURLS, 403);
    }
    else {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTCONTACTURLS);
    }
  }
  
  @Test
  public void testFindStudentContactURL() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, 3l);
    
    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTCONTACTURL, 403);
    }
    else {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTCONTACTURL);
    }
  }  

  @Test
  public void testDeleteStudentContactURL() throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/students/students/{STUDENTID}/contactURLs", TEST_STUDENT_ID);

    Long id = new Long(response.body().jsonPath().getInt("id"));

    response = given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, id);
    
    assertOk(response, studentPermissions, StudentPermissions.DELETE_STUDENTCONTACTURL, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
