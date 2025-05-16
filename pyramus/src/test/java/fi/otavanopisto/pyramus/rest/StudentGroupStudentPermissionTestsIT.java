package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentGroupPermissions;
import io.restassured.response.Response;

public class StudentGroupStudentPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentGroupPermissions studentGroupPermissions = new StudentGroupPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentGroupStudent(Role role) throws NoSuchFieldException {
    fi.otavanopisto.pyramus.rest.model.StudentGroupStudent entity = new fi.otavanopisto.pyramus.rest.model.StudentGroupStudent(null, 3l);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups/{ID}/students", 2l);

    assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.CREATE_STUDENTGROUPSTUDENT);
    
    if (response.getStatusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentGroupStudents(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studentGroups/{ID}/students", 1);

    assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.LIST_STUDENTGROUPSTUDENTS);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentGroupStudent(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studentGroups/{GROUPID}/students/{ID}", 1l, 1l);

    assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.FIND_STUDENTGROUPSTUDENT);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentGroupStudent(Role role) throws NoSuchFieldException {
    fi.otavanopisto.pyramus.rest.model.StudentGroupStudent entity = new fi.otavanopisto.pyramus.rest.model.StudentGroupStudent(null, 3l);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups/{ID}/students", 2l);

    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id);
    
    assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.DELETE_STUDENTGROUPSTUDENT, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id);
    }
  }
  
  
}
