package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.ModulePermissions;
import fi.pyramus.rest.model.ModuleComponent;

@RunWith(Parameterized.class)
public class ModuleComponentPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private ModulePermissions modulePermissions = new ModulePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public ModuleComponentPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateModuleComponent() throws NoSuchFieldException{
    Long moduleId = 1l;
    
    ModuleComponent component = new ModuleComponent(null,
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(component)
      .post("/modules/modules/{MODULEID}/components", moduleId);
    
    assertOk(response, modulePermissions, ModulePermissions.CREATE_MODULECOMPONENT, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.equals(200)){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAuthHeaders())
        .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id);
      }
    }
  }

  @Test
  public void testPermissionsListModuleComponents() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
      .get("/modules/modules/1/components");
    assertOk(response, modulePermissions, ModulePermissions.LIST_MODULECOMPONENTS, 200);
  }
  
  @Test
  public void testPermissionsFindModuleComponent() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/modules/modules/1/components/1");
    assertOk(response, modulePermissions, ModulePermissions.FIND_MODULECOMPONENT, 200);
  }
  
  @Test
  public void testPermissionsUpdateModuleComponent() throws NoSuchFieldException {
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
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    try {
      ModuleComponent updateComponent = new ModuleComponent(
          id,
          "updated", 
          "updated component",
          132d, 
          1l, 
          Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateComponent)
        .put("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id);
      assertOk(updateResponse, modulePermissions, ModulePermissions.UPDATE_MODULECOMPONENT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id);
    }
  }

  @Test
  public void testPermissionsDeleteModuleComponent() throws NoSuchFieldException{
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
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id);
    assertOk(deleteResponse, modulePermissions, ModulePermissions.DELETE_MODULECOMPONENT, 204);
    
    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.equals(204))
      given().headers(getAdminAuthHeaders())
        .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id);
  }
}
