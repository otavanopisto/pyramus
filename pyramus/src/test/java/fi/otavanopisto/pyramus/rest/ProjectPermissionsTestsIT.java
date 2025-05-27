package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.Arrays;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.ProjectPermissions;
import fi.otavanopisto.pyramus.rest.model.Project;
import io.restassured.response.Response;

public class ProjectPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private ProjectPermissions projectPermissions = new ProjectPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateProject(Role role) throws NoSuchFieldException {
    Project project = new Project(null, 
        "to be created", 
        "project to be created", 
        123d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(project)
      .post("/projects/projects");
    assertOk(role, response, projectPermissions, ProjectPermissions.CREATE_PROJECT, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/projects/projects/{ID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListProjects(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
        .get("/projects/projects"), projectPermissions, ProjectPermissions.LIST_PROJECTS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindProject(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/projects/projects/{ID}", 1), projectPermissions, ProjectPermissions.FIND_PROJECT, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateProjects(Role role) throws NoSuchFieldException {
    Project project = new Project(null, 
        "not updated", 
        "not updated project", 
        123d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(project)
      .post("/projects/projects");
      
    Long id = response.body().jsonPath().getLong("id");
    try {
      Project updateProject = new Project(id, 
        "updated", 
        "updated project", 
        234d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag2", "tag3"), 
        Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateProject)
        .put("/projects/projects/{ID}", id);
      assertOk(role, updateResponse, projectPermissions, ProjectPermissions.UPDATE_PROJECT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/projects/projects/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteProject(Role role) throws NoSuchFieldException {
    Project project = new Project(null, 
        "to be deleted", 
        "to be deleted", 
        123d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(project)
      .post("/projects/projects");

    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/projects/projects/{ID}", id);
    assertOk(role, deleteResponse, projectPermissions, ProjectPermissions.DELETE_PROJECT, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/projects/projects/{ID}?permanent=true", id);    
  }
}
