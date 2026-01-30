package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Address;
import io.restassured.response.Response;

public class StudentAddressPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentPermissions studentPermissions = new StudentPermissions();
  
  private final static long TEST_STUDENT_ID = 3l;
  private static final long SECONDARY_TEST_STUDENT_ID = 13L;
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentAddress(Role role) throws NoSuchFieldException {
    Address address = new Address(null, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(address)
      .post("/students/students/{ID}/addresses", TEST_STUDENT_ID);

    assertOk(role, response, studentPermissions, StudentPermissions.CREATE_STUDENTADDRESS);

    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentAddressOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      Address address = new Address(null, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
      
      Response response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(address)
        .post("/students/students/{ID}/addresses", getUserIdForRole(role));
      
      response
        .then()
        .assertThat()
        .statusCode(200);
  
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/addresses/{ID}", getUserIdForRole(role), id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentAddresses(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{ID}/addresses", TEST_STUDENT_ID);
    
    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTADDRESSS, 403);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTADDRESSS);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentAddressesStudent2(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{ID}/addresses", SECONDARY_TEST_STUDENT_ID);
    
    // This should be ok for all roles as the group restricted study guider can
    // also access this user via studentgroup 2.
    assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTADDRESSS, 204);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentAddressesOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      given()
        .headers(getAuthHeaders(role))
        .get("/students/students/{ID}/addresses", getUserIdForRole(role))
        .then()
        .assertThat()
        .statusCode(200);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentAddress(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, 3l);

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTADDRESS, 403);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTADDRESS);
    }
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentAddressOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      given()
        .headers(getAuthHeaders(role))
        .get("/students/students/{STUDENTID}/addresses/{ID}", getUserIdForRole(role), 8l)
        .then()
        .assertThat()
        .statusCode(200);
    }
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentAddress(Role role) throws NoSuchFieldException {
    Address address = new Address(null, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/students/students/{STUDENTID}/addresses", TEST_STUDENT_ID);

    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, id);

    assertOk(role, response, studentPermissions, StudentPermissions.DELETE_STUDENTADDRESS, 204);
    
    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
