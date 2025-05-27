package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.ProjectPermissions;
import fi.otavanopisto.pyramus.rest.model.ProjectModule;
import fi.otavanopisto.pyramus.rest.model.ProjectModuleOptionality;
import io.restassured.response.Response;

public class ProjectModulePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private ProjectPermissions projectPermissions = new ProjectPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateProjectModule(Role role) throws NoSuchFieldException {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);
    
    assertOk(role, response, projectPermissions, ProjectPermissions.CREATE_PROJECTMODULE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListProjectModules(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
      .get("/projects/projects/{PROJECTID}/modules", 1l);
    assertOk(role, response, projectPermissions, ProjectPermissions.LIST_PROJECTMODULES, 200);
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindProjectModule(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/projects/projects/{PROJECTID}/modules/{ID}", 1l, 1l), projectPermissions, ProjectPermissions.FIND_PROJECTMODULE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermisisonsUpdateProjectModule(Role role) throws NoSuchFieldException {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);

    Long id = response.body().jsonPath().getLong("id");
    try {
      ProjectModule updateProjectModule = new ProjectModule(id, 1l, ProjectModuleOptionality.MANDATORY);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateProjectModule)
        .put("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
      assertOk(role, updateResponse, projectPermissions, ProjectPermissions.UPDATE_PROJECTMODULE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteProjectModule(Role role) throws NoSuchFieldException {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);

    Long id = response.body().jsonPath().getLong("id");
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
    assertOk(role, deleteResponse, projectPermissions, ProjectPermissions.DELETE_PROJECTMODULE, 204);
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id);
  }
}
