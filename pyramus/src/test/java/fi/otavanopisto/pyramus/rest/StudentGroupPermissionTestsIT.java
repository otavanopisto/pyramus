package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentGroupPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.StudentGroup;

@RunWith(Parameterized.class)
public class StudentGroupPermissionTestsIT extends AbstractRESTPermissionsTest {

  private static final Long TEST_ORGANIZATION_ID = 1l;
  
  public StudentGroupPermissionTestsIT(String role) {
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
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @Test
  public void testCreateStudentGroup() throws NoSuchFieldException {
    StudentGroup entity = new StudentGroup(null, 
        "to be created", 
        "student group to be created", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE,
        TEST_ORGANIZATION_ID, // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    assertOk(response, studentGroupPermissions, StudentGroupPermissions.CREATE_STUDENTGROUP);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testListStudentGroups() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studentGroups");

    assertOk(response, studentGroupPermissions, StudentGroupPermissions.LIST_STUDENTGROUPS);
  }
  
  @Test
  public void testFindStudentGroup() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studentGroups/{ID}", 1);

    if (!roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION))
      assertOk(response, studentGroupPermissions, StudentGroupPermissions.FIND_STUDENTGROUP);
    else
      assertOk(response, studentGroupPermissions, StudentGroupPermissions.FIND_STUDENTGROUP, 403);
  }
  
  @Test
  public void testUpdateStudentGroup() throws NoSuchFieldException {
    StudentGroup entity = new StudentGroup(null, 
        "not updated", 
        "not updated student group", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE,
        TEST_ORGANIZATION_ID, // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentGroup updateEntity = new StudentGroup(id, 
          "updated", 
          "updated student group", 
          getDate(2015, 7, 7), 
          null, 
          null, 
          null, 
          null, 
          Arrays.asList("tag2", "tag3"), 
          Boolean.FALSE,
          TEST_ORGANIZATION_ID, // organizationId
          Boolean.FALSE);
      
      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/students/studentGroups/{ID}", id);
      
      assertOk(response, studentGroupPermissions, StudentGroupPermissions.UPDATE_STUDENTGROUP);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteStudentGroup() throws NoSuchFieldException {
    StudentGroup entity = new StudentGroup(null, 
        "to be created", 
        "student group to be created", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE,
        TEST_ORGANIZATION_ID, // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/studentGroups/{ID}", id);
    
    assertOk(response, studentGroupPermissions, StudentGroupPermissions.DELETE_STUDENTGROUP, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/studentGroups/{ID}?permanent=true", id);
  }
  
}