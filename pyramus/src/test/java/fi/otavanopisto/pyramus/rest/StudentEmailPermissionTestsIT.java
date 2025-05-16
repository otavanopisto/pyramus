package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Email;
import io.restassured.response.Response;

public class StudentEmailPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentPermissions studentPermissions = new StudentPermissions();
  
  private final static long TEST_STUDENT_ID = 3l;
  private static final long SECONDARY_TEST_STUDENT_ID = 13L;

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentEmail(Role role) throws NoSuchFieldException {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(email)
      .post("/students/students/{ID}/emails", TEST_STUDENT_ID);

    assertOk(role, response, studentPermissions, StudentPermissions.CREATE_STUDENTEMAIL);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentEmailOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
      
      Response response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(email)
        .post("/students/students/{ID}/emails", getUserIdForRole(role));
      
      response
        .then()
        .assertThat()
        .statusCode(200);
      
      int id = response.body().jsonPath().getInt("id");
      
      given()
        .headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/emails/{ID}", getUserIdForRole(role), id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentEmails(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{ID}/emails", TEST_STUDENT_ID);

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS, 403);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentEmailsStudent2(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{ID}/emails", SECONDARY_TEST_STUDENT_ID);

    // This should be ok for all roles as the group restricted study guider can
    // also access this user via studentgroup 2.
    assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTEMAILS, 204);
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentEmailsOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      given()
        .headers(getAuthHeaders(role))
        .get("/students/students/{ID}/emails", getUserIdForRole(role))
        .then()
        .assertThat()
        .statusCode(200);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentEmail(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, 3l);

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTEMAIL, 403);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTEMAIL);
    }
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentEmailOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      given()
        .headers(getAuthHeaders(role))
        .get("/students/students/{STUDENTID}/emails/{ID}", getUserIdForRole(role), 10l)
        .then()
        .assertThat()
        .statusCode(200);
    }
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentEmail(Role role) throws NoSuchFieldException {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/students/students/{STUDENTID}/emails", TEST_STUDENT_ID);

    Long id = response.body().jsonPath().getLong("id");

    response = given().headers(getAuthHeaders(role))
      .delete("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, id);
    
    assertOk(role, response, studentPermissions, StudentPermissions.DELETE_STUDENTEMAIL, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/emails/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
