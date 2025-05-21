package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.PhoneNumber;
import io.restassured.response.Response;

public class StudentPhoneNumberPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentPermissions studentPermissions = new StudentPermissions();
  
  private final static long TEST_STUDENT_ID = 3l;
  private static final long SECONDARY_TEST_STUDENT_ID = 13L;

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentPhoneNumber(Role role) throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(phoneNumber)
      .post("/students/students/{ID}/phoneNumbers", TEST_STUDENT_ID);

    assertOk(role, response, studentPermissions, StudentPermissions.CREATE_STUDENTPHONENUMBER);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentPhoneNumberOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
      
      Response response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(phoneNumber)
        .post("/students/students/{ID}/phoneNumbers", getUserIdForRole(role));
      
      response
        .then()
        .assertThat()
        .statusCode(200);

      int id = response.body().jsonPath().getInt("id");
        
      given()
        .headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", getUserIdForRole(role), id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentPhoneNumbers(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{ID}/phoneNumbers", TEST_STUDENT_ID);

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTPHONENUMBERS, 403);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTPHONENUMBERS);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentPhoneNumbersStudent2(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{ID}/phoneNumbers", SECONDARY_TEST_STUDENT_ID);

    // This should be ok for all roles as the group restricted study guider can
    // also access this user via studentgroup 2.
    assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTPHONENUMBERS, 204);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentPhoneNumbersOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      given().headers(getAuthHeaders(role))
        .get("/students/students/{ID}/phoneNumbers", getUserIdForRole(role))
        .then()
        .assertThat()
        .statusCode(200);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentPhoneNumber(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, 3l);
    
    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTPHONENUMBER, 403);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTPHONENUMBER);
    }
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentPhoneNumberOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      given().headers(getAuthHeaders(role))
        .get("/students/students/{STUDENTID}/phoneNumbers/{ID}", getUserIdForRole(role), 8l)        
        .then()
        .assertThat()
        .statusCode(200);
    }
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentPhoneNumber(Role role) throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/students/students/{STUDENTID}/phoneNumbers", TEST_STUDENT_ID);

    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id);
    
    assertOk(role, response, studentPermissions, StudentPermissions.DELETE_STUDENTPHONENUMBER, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
