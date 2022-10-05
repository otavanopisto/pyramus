package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.ProjectPermissions;
import fi.otavanopisto.pyramus.rest.model.ProjectModule;
import fi.otavanopisto.pyramus.rest.model.ProjectModuleOptionality;

@RunWith(Parameterized.class)
public class ProjectModulePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private ProjectPermissions projectPermissions = new ProjectPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public ProjectModulePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateProjectModule() throws NoSuchFieldException {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);
    
    assertOk(response, projectPermissions, ProjectPermissions.CREATE_PROJECTMODULE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
      }
    }
  }
  
  @Test
  public void testPermissionsListProjectModules() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
      .get("/projects/projects/{PROJECTID}/modules", 1l);
    assertOk(response, projectPermissions, ProjectPermissions.LIST_PROJECTMODULES, 200);
  }

  @Test
  public void testPermissionsFindProjectModule() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/projects/projects/{PROJECTID}/modules/{ID}", 1l, 1l), projectPermissions, ProjectPermissions.FIND_PROJECTMODULE, 200);
  }
  
  @Test
  public void testPermisisonsUpdateProjectModule() throws NoSuchFieldException {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);

    Long id = response.body().jsonPath().getLong("id");
    try {
      ProjectModule updateProjectModule = new ProjectModule(id, 1l, ProjectModuleOptionality.MANDATORY);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateProjectModule)
        .put("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
      assertOk(updateResponse, projectPermissions, ProjectPermissions.UPDATE_PROJECTMODULE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
    }
  }
  
  @Test
  public void testPermissionsDeleteProjectModule() throws NoSuchFieldException {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);

    Long id = response.body().jsonPath().getLong("id");
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
    assertOk(deleteResponse, projectPermissions, ProjectPermissions.DELETE_PROJECTMODULE, 204);
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
  }
}
