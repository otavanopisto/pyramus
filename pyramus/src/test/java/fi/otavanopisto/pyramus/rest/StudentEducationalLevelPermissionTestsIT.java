package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentEducationalLevelPermissions;
import fi.otavanopisto.pyramus.rest.model.StudentEducationalLevel;

@RunWith(Parameterized.class)
public class StudentEducationalLevelPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentEducationalLevelPermissionTestsIT(String role) {
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
  
  private StudentEducationalLevelPermissions studentEducationalLevelPermissions = new StudentEducationalLevelPermissions();
  
  @Test
  public void testCreateStudentEducationalLevel() throws NoSuchFieldException {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");

    assertOk(response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.CREATE_STUDENTEDUCATIONALLEVEL);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/educationalLevels/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void listStudentEducationalLevels() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/educationalLevels");
    
    assertOk(response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.LIST_STUDENTEDUCATIONALLEVELS);
  }
  
  @Test
  public void testFindStudentEducationalLevel() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/educationalLevels/{ID}", 1);

    assertOk(response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.FIND_STUDENTEDUCATIONALLEVEL);
  }
  
  @Test
  public void testUpdateStudentEducationalLevel() throws NoSuchFieldException {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentEducationalLevel updateStudentEducationalLevel = new StudentEducationalLevel(id, "Updated", Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudentEducationalLevel)
        .put("/students/educationalLevels/{ID}", id);

      assertOk(response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.UPDATE_STUDENTEDUCATIONALLEVEL);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/educationalLevels/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentEducationalLevel() throws NoSuchFieldException {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/educationalLevels/{ID}", id);
    
    assertOk(response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.DELETE_STUDENTEDUCATIONALLEVEL, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/educationalLevels/{ID}?permanent=true", id);
  }
}
