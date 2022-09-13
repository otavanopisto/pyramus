package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentGroupPermissions;

@RunWith(Parameterized.class)
public class StudentGroupStudentPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudentGroupStudentPermissionTestsIT(String role) {
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
  
  private StudentGroupPermissions studentGroupPermissions = new StudentGroupPermissions();
  
  @Test
  public void testCreateStudentGroupStudent() throws NoSuchFieldException {
    fi.otavanopisto.pyramus.rest.model.StudentGroupStudent entity = new fi.otavanopisto.pyramus.rest.model.StudentGroupStudent(null, 3l);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups/{ID}/students", 2l);

    assertOk(response, studentGroupPermissions, StudentGroupPermissions.CREATE_STUDENTGROUPSTUDENT);
    
    if (response.getStatusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id);
    }
  }
  
  @Test
  public void testListStudentGroupStudents() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studentGroups/{ID}/students", 1);

    assertOk(response, studentGroupPermissions, StudentGroupPermissions.LIST_STUDENTGROUPSTUDENTS);
  }
  
  @Test
  public void testFindStudentGroupStudent() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studentGroups/{GROUPID}/students/{ID}", 1l, 1l);

    assertOk(response, studentGroupPermissions, StudentGroupPermissions.FIND_STUDENTGROUPSTUDENT);
  }
  
  @Test
  public void testDeleteStudentGroupStudent() throws NoSuchFieldException {
    fi.otavanopisto.pyramus.rest.model.StudentGroupStudent entity = new fi.otavanopisto.pyramus.rest.model.StudentGroupStudent(null, 3l);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups/{ID}/students", 2l);

    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders())
      .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id);
    
    assertOk(response, studentGroupPermissions, StudentGroupPermissions.DELETE_STUDENTGROUPSTUDENT, 204);

    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id);
    }
  }
  
  
}
