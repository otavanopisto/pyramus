package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Email;

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
  private static final long SECONDARY_TEST_STUDENT_ID = 13L;

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
      
      response
        .then()
        .assertThat()
        .statusCode(200);
      
      int id = response.body().jsonPath().getInt("id");
      
      given()
        .headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/emails/{ID}", getUserIdForRole(getRole()), id);
    }
  }
  
  @Test
  public void testListStudentEmails() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/emails", TEST_STUDENT_ID);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS, 403);
    } else {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS);
    }
  }
  
  @Test
  public void testListStudentEmailsStudent2() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/emails", SECONDARY_TEST_STUDENT_ID);

    // This should be ok for all roles as the group restricted study guider can
    // also access this user via studentgroup 2.
    assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS, 204);
  }

  @Test
  public void testListStudentEmailsOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      given()
        .headers(getAuthHeaders())
        .get("/students/students/{ID}/emails", getUserIdForRole(this.getRole()))
        .then()
        .assertThat()
        .statusCode(200);
    }
  }
  
  @Test
  public void testFindStudentEmail() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, 3l);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTEMAIL, 403);
    } else {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTEMAIL);
    }
  }  

  @Test
  public void testFindStudentEmailOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      given()
        .headers(getAuthHeaders())
        .get("/students/students/{STUDENTID}/emails/{ID}", getUserIdForRole(getRole()), 10l)
        .then()
        .assertThat()
        .statusCode(200);
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
