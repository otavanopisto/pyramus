package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.ProjectPermissions;
import fi.otavanopisto.pyramus.rest.model.Project;

@RunWith(Parameterized.class)
public class ProjectPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private ProjectPermissions projectPermissions = new ProjectPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public ProjectPermissionsTestsIT(String role) {
    this.role = role;
  }
    
  @Test
  public void testPermissionsCreateProject() throws NoSuchFieldException {
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
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(project)
      .post("/projects/projects");
    assertOk(response, projectPermissions, ProjectPermissions.CREATE_PROJECT, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/projects/projects/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListProjects() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
        .get("/projects/projects"), projectPermissions, ProjectPermissions.LIST_PROJECTS, 200);
  }
  
  @Test
  public void testPermissionsFindProject() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/projects/projects/{ID}", 1), projectPermissions, ProjectPermissions.FIND_PROJECT, 200);
  }
  
  @Test
  public void testPermissionsUpdateProjects() throws NoSuchFieldException {
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

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateProject)
        .put("/projects/projects/{ID}", id);
      assertOk(updateResponse, projectPermissions, ProjectPermissions.UPDATE_PROJECT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/projects/projects/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteProject() throws NoSuchFieldException {
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
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/projects/projects/{ID}", id);
    assertOk(deleteResponse, projectPermissions, ProjectPermissions.DELETE_PROJECT, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/projects/projects/{ID}?permanent=true", id);    
  }
}
