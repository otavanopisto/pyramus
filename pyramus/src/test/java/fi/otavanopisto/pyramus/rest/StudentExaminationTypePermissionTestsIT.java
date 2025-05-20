package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentExaminationTypePermissions;
import fi.otavanopisto.pyramus.rest.model.StudentExaminationType;
import io.restassured.response.Response;

public class StudentExaminationTypePermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentExaminationTypePermissions studentExaminationTypePermissions = new StudentExaminationTypePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentExaminationType(Role role) throws NoSuchFieldException {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");

    assertOk(role, response, studentExaminationTypePermissions, StudentExaminationTypePermissions.CREATE_STUDENTEXAMINATIONTYPE);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/examinationTypes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void listStudentExaminationTypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/examinationTypes");
    
    assertOk(role, response, studentExaminationTypePermissions, StudentExaminationTypePermissions.LIST_STUDENTEXAMINATIONTYPES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentExaminationType(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/examinationTypes/{ID}", 1);

    assertOk(role, response, studentExaminationTypePermissions, StudentExaminationTypePermissions.FIND_STUDENTEXAMINATIONTYPE);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudentExaminationType(Role role) throws NoSuchFieldException {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");

    Long id = response.body().jsonPath().getLong("id");
    try {
      StudentExaminationType updateStudentExaminationType = new StudentExaminationType(id, "Updated", Boolean.FALSE);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudentExaminationType)
        .put("/students/examinationTypes/{ID}", id);
      
      assertOk(role, response, studentExaminationTypePermissions, StudentExaminationTypePermissions.UPDATE_STUDENTEXAMINATIONTYPE);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/examinationTypes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentExaminationType(Role role) throws NoSuchFieldException {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/examinationTypes/{ID}", id);
    
    assertOk(role, response, studentExaminationTypePermissions, StudentExaminationTypePermissions.DELETE_STUDENTEXAMINATIONTYPE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/examinationTypes/{ID}?permanent=true", id);
  }
}