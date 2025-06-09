package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.Arrays;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentGroupPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.StudentGroup;
import io.restassured.response.Response;

public class StudentGroupPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private static final Long TEST_ORGANIZATION_ID = 1l;
  
  private StudentGroupPermissions studentGroupPermissions = new StudentGroupPermissions();
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentGroup(Role role) throws NoSuchFieldException {
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
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.CREATE_STUDENTGROUP);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentGroups(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studentGroups");

    assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.LIST_STUDENTGROUPS);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentGroup(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studentGroups/{ID}", 1);

    if (!roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION))
      assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.FIND_STUDENTGROUP);
    else
      assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.FIND_STUDENTGROUP, 403);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudentGroup(Role role) throws NoSuchFieldException {
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

    Long id = response.body().jsonPath().getLong("id");
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
      
      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateEntity)
        .put("/students/studentGroups/{ID}", id);
      
      assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.UPDATE_STUDENTGROUP);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studentGroups/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentGroup(Role role) throws NoSuchFieldException {
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

    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/studentGroups/{ID}", id);
    
    assertOk(role, response, studentGroupPermissions, StudentGroupPermissions.DELETE_STUDENTGROUP, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/studentGroups/{ID}?permanent=true", id);
  }
  
}