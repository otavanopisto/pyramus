package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.ModulePermissions;
import fi.otavanopisto.pyramus.rest.model.ModuleComponent;
import io.restassured.response.Response;

public class ModuleComponentPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private ModulePermissions modulePermissions = new ModulePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateModuleComponent(Role role) throws NoSuchFieldException{
    Long moduleId = 1l;
    
    ModuleComponent component = new ModuleComponent(null,
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(component)
      .post("/modules/modules/{MODULEID}/components", moduleId);
    
    assertOk(role, response, modulePermissions, ModulePermissions.CREATE_MODULECOMPONENT, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListModuleComponents(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
      .get("/modules/modules/1/components");
    assertOk(role, response, modulePermissions, ModulePermissions.LIST_MODULECOMPONENTS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindModuleComponent(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/modules/modules/1/components/1");
    assertOk(role, response, modulePermissions, ModulePermissions.FIND_MODULECOMPONENT, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateModuleComponent(Role role) throws NoSuchFieldException {
    Long moduleId = 1l;
    
    ModuleComponent moduleComponent = new ModuleComponent(null,
        "not updated", 
        "not updated component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(moduleComponent)
      .post("/modules/modules/{MODULEID}/components", moduleId);
     
    Long id = response.body().jsonPath().getLong("id");
    
    try {
      ModuleComponent updateComponent = new ModuleComponent(
          id,
          "updated", 
          "updated component",
          132d, 
          1l, 
          Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateComponent)
        .put("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id);
      assertOk(role, updateResponse, modulePermissions, ModulePermissions.UPDATE_MODULECOMPONENT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id);
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteModuleComponent(Role role) throws NoSuchFieldException{
    Long moduleId = 1l;
    
    ModuleComponent moduleComponent = new ModuleComponent(null,
        "to be deleted", 
        "component to be deleted",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(moduleComponent)
      .post("/modules/modules/{MODULEID}/components", moduleId);
    
    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id);
    assertOk(role, deleteResponse, modulePermissions, ModulePermissions.DELETE_MODULECOMPONENT, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id);
  }
}
