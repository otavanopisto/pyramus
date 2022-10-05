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
import fi.otavanopisto.pyramus.rest.model.Address;

@RunWith(Parameterized.class)
public class StudentAddressPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentAddressPermissionTestsIT(String role) {
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
  public void testCreateStudentAddress() throws NoSuchFieldException {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/students/students/{ID}/addresses", TEST_STUDENT_ID);

    assertOk(response, studentPermissions, StudentPermissions.CREATE_STUDENTADDRESS);

    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, id);
    }
  }
  
  @Test
  public void testCreateStudentAddressOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
      
      Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(address)
        .post("/students/students/{ID}/addresses", getUserIdForRole(getRole()));
      
      response
        .then()
        .assertThat()
        .statusCode(200);
  
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/addresses/{ID}", getUserIdForRole(getRole()), id);
    }
  }
  
  @Test
  public void testListStudentAddresses() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/addresses", TEST_STUDENT_ID);
    
    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTADDRESSS, 403);
    } else {
      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTADDRESSS);
    }
  }
  
  @Test
  public void testListStudentAddressesStudent2() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/addresses", SECONDARY_TEST_STUDENT_ID);
    
    // This should be ok for all roles as the group restricted study guider can
    // also access this user via studentgroup 2.
    assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTADDRESSS, 204);
  }
  
  @Test
  public void testListStudentAddressesOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      given()
        .headers(getAuthHeaders())
        .get("/students/students/{ID}/addresses", getUserIdForRole(getRole()))
        .then()
        .assertThat()
        .statusCode(200);
    }
  }
  
  @Test
  public void testFindStudentAddress() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, 3l);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTADDRESS, 403);
    } else {
      assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENTADDRESS);
    }
  }  

  @Test
  public void testFindStudentAddressOwner() throws NoSuchFieldException {
    if (Role.STUDENT.name().equals(this.role)) {
      given()
        .headers(getAuthHeaders())
        .get("/students/students/{STUDENTID}/addresses/{ID}", getUserIdForRole(getRole()), 8l)
        .then()
        .assertThat()
        .statusCode(200);
    }
  }  

  @Test
  public void testDeleteStudentAddress() throws NoSuchFieldException {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/students/students/{STUDENTID}/addresses", TEST_STUDENT_ID);

    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, id);

    assertOk(response, studentPermissions, StudentPermissions.DELETE_STUDENTADDRESS, 204);
    
    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/addresses/{ID}", TEST_STUDENT_ID, id);
    }
  }
}
