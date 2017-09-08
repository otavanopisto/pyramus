package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentExaminationTypePermissions;
import fi.otavanopisto.pyramus.rest.model.StudentExaminationType;

@RunWith(Parameterized.class)
public class StudentExaminationTypePermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentExaminationTypePermissionTestsIT(String role) {
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
  
  private StudentExaminationTypePermissions studentExaminationTypePermissions = new StudentExaminationTypePermissions();
  
  @Test
  public void testCreateStudentExaminationType() throws NoSuchFieldException {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");

    assertOk(response, studentExaminationTypePermissions, StudentExaminationTypePermissions.CREATE_STUDENTEXAMINATIONTYPE);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/examinationTypes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void listStudentExaminationTypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/examinationTypes");
    
    assertOk(response, studentExaminationTypePermissions, StudentExaminationTypePermissions.LIST_STUDENTEXAMINATIONTYPES);
  }
  
  @Test
  public void testFindStudentExaminationType() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/examinationTypes/{ID}", 1);

    assertOk(response, studentExaminationTypePermissions, StudentExaminationTypePermissions.FIND_STUDENTEXAMINATIONTYPE);
  }
  
  @Test
  public void testUpdateStudentExaminationType() throws NoSuchFieldException {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentExaminationType updateStudentExaminationType = new StudentExaminationType(id, "Updated", Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudentExaminationType)
        .put("/students/examinationTypes/{ID}", id);
      
      assertOk(response, studentExaminationTypePermissions, StudentExaminationTypePermissions.UPDATE_STUDENTEXAMINATIONTYPE);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/examinationTypes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteStudentExaminationType() throws NoSuchFieldException {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/examinationTypes/{ID}", id);
    
    assertOk(response, studentExaminationTypePermissions, StudentExaminationTypePermissions.DELETE_STUDENTEXAMINATIONTYPE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/examinationTypes/{ID}?permanent=true", id);
  }
}