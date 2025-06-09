package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentEducationalLevelPermissions;
import fi.otavanopisto.pyramus.rest.model.StudentEducationalLevel;
import io.restassured.response.Response;

public class StudentEducationalLevelPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentEducationalLevelPermissions studentEducationalLevelPermissions = new StudentEducationalLevelPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentEducationalLevel(Role role) throws NoSuchFieldException {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");

    assertOk(role, response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.CREATE_STUDENTEDUCATIONALLEVEL);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/educationalLevels/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void listStudentEducationalLevels(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/educationalLevels");
    
    assertOk(role, response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.LIST_STUDENTEDUCATIONALLEVELS);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentEducationalLevel(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/educationalLevels/{ID}", 1);

    assertOk(role, response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.FIND_STUDENTEDUCATIONALLEVEL);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudentEducationalLevel(Role role) throws NoSuchFieldException {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");

    Long id = response.body().jsonPath().getLong("id");
    try {
      StudentEducationalLevel updateStudentEducationalLevel = new StudentEducationalLevel(id, "Updated", Boolean.FALSE);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudentEducationalLevel)
        .put("/students/educationalLevels/{ID}", id);

      assertOk(role, response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.UPDATE_STUDENTEDUCATIONALLEVEL);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/educationalLevels/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentEducationalLevel(Role role) throws NoSuchFieldException {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/educationalLevels/{ID}", id);
    
    assertOk(role, response, studentEducationalLevelPermissions, StudentEducationalLevelPermissions.DELETE_STUDENTEDUCATIONALLEVEL, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/educationalLevels/{ID}?permanent=true", id);
  }
}
