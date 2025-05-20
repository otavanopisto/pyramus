package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactURL;
import io.restassured.response.Response;

public class StudentContactURLPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentPermissions studentPermissions = new StudentPermissions();
  
  private final static long TEST_STUDENT_ID = 3l;

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentContactURL(Role role) throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(contactURL)
      .post("/students/students/{ID}/contactURLs", TEST_STUDENT_ID);

    assertOk(role, response, studentPermissions, StudentPermissions.CREATE_STUDENTCONTACTURL);

    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentContactURLs(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{ID}/contactURLs", TEST_STUDENT_ID);

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTCONTACTURLS, 403);
    }
    else {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTCONTACTURLS);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentContactURL(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, 3l);
    
    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTCONTACTURL, 403);
    }
    else {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTCONTACTURL);
    }
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentContactURL(Role role) throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/students/students/{STUDENTID}/contactURLs", TEST_STUDENT_ID);

    Long id = response.body().jsonPath().getLong("id");

    response = given().headers(getAuthHeaders(role))
      .delete("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, id);
    
    assertOk(role, response, studentPermissions, StudentPermissions.DELETE_STUDENTCONTACTURL, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/contactURLs/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
