package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.rest.controller.permissions.StudentPermissions;
import fi.pyramus.rest.model.Email;

@RunWith(Parameterized.class)
public class StudentEmailPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentEmailPermissionTestsIT(String role) {
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
  public void testCreateStudentEmail() throws NoSuchFieldException {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/students/students/{ID}/emails", TEST_STUDENT_ID);

    assertOk(response, studentPermissions, StudentPermissions.CREATE_STUDENTEMAIL);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @Test
  public void testCreateStudentEmailOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
      
      Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(email)
        .post("/students/students/{ID}/emails", getUserIdForRole(getRole()));
  
      assertOk(response, studentPermissions, StudentPermissions.CREATE_STUDENTEMAIL);
      
      if (response.getStatusCode() == 200) {
        int id = response.body().jsonPath().getInt("id");
        
        given().headers(getAdminAuthHeaders())
          .delete("/students/students/{STUDENTID}/emails/{ID}", getUserIdForRole(getRole()), id);
      }
    }
  }
  
  @Test
  public void testListStudentEmails() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/emails", TEST_STUDENT_ID);

    assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS);
  }
  
  @Test
  public void testListStudentEmailsOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      Response response = given().headers(getAuthHeaders())
        .get("/students/students/{ID}/emails", getUserIdForRole(this.getRole()));
  
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS);
    }
  }
  
  @Test
  public void testFindStudentEmail() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, 3l);

    assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTEMAIL);
  }  

  @Test
  public void testFindStudentEmailOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      Response response = given().headers(getAuthHeaders())
        .get("/students/students/{STUDENTID}/emails/{ID}", getUserIdForRole(getRole()), 8l);
  
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTEMAIL);
    }
  }  

  @Test
  public void testDeleteStudentEmail() throws NoSuchFieldException {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/students/students/{STUDENTID}/emails", TEST_STUDENT_ID);

    Long id = new Long(response.body().jsonPath().getInt("id"));

    response = given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, id);
    
    assertOk(response, studentPermissions, StudentPermissions.DELETE_STUDENTEMAIL, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
