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
import fi.otavanopisto.pyramus.rest.model.PhoneNumber;

@RunWith(Parameterized.class)
public class StudentPhoneNumberPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentPhoneNumberPermissionTestsIT(String role) {
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
  public void testCreateStudentPhoneNumber() throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/students/students/{ID}/phoneNumbers", TEST_STUDENT_ID);

    assertOk(response, studentPermissions, StudentPermissions.CREATE_STUDENTPHONENUMBER);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @Test
  public void testCreateStudentPhoneNumberOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
      
      Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(phoneNumber)
        .post("/students/students/{ID}/phoneNumbers", getUserIdForRole(this.getRole()));
      
      response
        .then()
        .assertThat()
        .statusCode(200);

      int id = response.body().jsonPath().getInt("id");
        
      given()
        .headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", getUserIdForRole(this.getRole()), id);
    }
  }
  
  @Test
  public void testListStudentPhoneNumbers() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/phoneNumbers", TEST_STUDENT_ID);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTPHONENUMBERS, 403);
    } else {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTPHONENUMBERS);
    }
  }
  
  @Test
  public void testListStudentPhoneNumbersStudent2() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/phoneNumbers", SECONDARY_TEST_STUDENT_ID);

    // This should be ok for all roles as the group restricted study guider can
    // also access this user via studentgroup 2.
    assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTPHONENUMBERS, 204);
  }
  
  @Test
  public void testListStudentPhoneNumbersOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      given().headers(getAuthHeaders())
        .get("/students/students/{ID}/phoneNumbers", getUserIdForRole(this.role))
        .then()
        .assertThat()
        .statusCode(200);
    }
  }
  
  @Test
  public void testFindStudentPhoneNumber() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, 3l);
    
    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTPHONENUMBER, 403);
    } else {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTPHONENUMBER);
    }
  }  

  @Test
  public void testFindStudentPhoneNumberOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      given().headers(getAuthHeaders())
        .get("/students/students/{STUDENTID}/phoneNumbers/{ID}", getUserIdForRole(this.role), 8l)        
        .then()
        .assertThat()
        .statusCode(200);
    }
  }  

  @Test
  public void testDeleteStudentPhoneNumber() throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/students/students/{STUDENTID}/phoneNumbers", TEST_STUDENT_ID);

    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id);
    
    assertOk(response, studentPermissions, StudentPermissions.DELETE_STUDENTPHONENUMBER, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
