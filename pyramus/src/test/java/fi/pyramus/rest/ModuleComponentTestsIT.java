package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.ModuleComponent;

public class ModuleComponentTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateModuleComponent() {
    Long moduleId = 1l;
    
    ModuleComponent component = new ModuleComponent(null,
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given()
      .contentType("application/json")
      .body(component)
      .post("/modules/modules/{MODULEID}/components", moduleId);
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(component.getName()))
      .body("description", is( component.getDescription() ))
      .body("length", is( component.getLength().floatValue() ))
      .body("lengthUnitId", is( component.getLengthUnitId().intValue() ))
      .body("archived", is( component.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListModuleComponents() {
    given()
      .get("/modules/modules/1/components")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Test Module #1 component #1" ))
      .body("description[0]", is( "Module component for testing" ))
      .body("length[0]", is( 1f ))
      .body("lengthUnitId[0]", is( 1 ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Test Module #1 component #2" ))
      .body("description[1]", is( "Module component for testing" ))
      .body("length[1]", is( 123f ))
      .body("lengthUnitId[1]", is( 1 ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindModuleComponent() {
    given()
      .get("/modules/modules/1/components/1")
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Test Module #1 component #1" ))
      .body("description", is( "Module component for testing" ))
      .body("length", is( 1f ))
      .body("lengthUnitId", is( 1 ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateModuleComponent() {
    Long moduleId = 1l;
    
    ModuleComponent moduleComponent = new ModuleComponent(null,
        "not updated", 
        "not updated component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given()
      .contentType("application/json")
      .body(moduleComponent)
      .post("/modules/modules/{MODULEID}/components", moduleId);
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    try {
      response.then()
        .body("id", not(is((Long) null)))
        .body("name", is(moduleComponent.getName()))
        .body("description", is( moduleComponent.getDescription() ))
        .body("length", is( moduleComponent.getLength().floatValue() ))
        .body("lengthUnitId", is( moduleComponent.getLengthUnitId().intValue() ))
        .body("archived", is( moduleComponent.getArchived() ));
      
      ModuleComponent updateComponent = new ModuleComponent(
          id,
          "updated", 
          "updated component",
          132d, 
          1l, 
          Boolean.FALSE);

      given()
        .contentType("application/json")
        .body(updateComponent)
        .put("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id)
        .then()
        .statusCode(200)
        .body("id", is(updateComponent.getId().intValue()))
        .body("name", is(updateComponent.getName()))
        .body("description", is( updateComponent.getDescription() ))
        .body("length", is( updateComponent.getLength().floatValue() ))
        .body("lengthUnitId", is( updateComponent.getLengthUnitId().intValue() ))
        .body("archived", is( updateComponent.getArchived() ));  

    } finally {
      given()
        .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id)
        .then()
        .statusCode(204);
    }
  }

  @Test
  public void testDeleteModuleComponent() {
    Long moduleId = 1l;
    
    ModuleComponent moduleComponent = new ModuleComponent(null,
        "to be deleted", 
        "component to be deleted",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given()
      .contentType("application/json")
      .body(moduleComponent)
      .post("/modules/modules/{MODULEID}/components", moduleId);
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id)
      .then()
      .statusCode(204);
    
    given().get("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/modules/modules/{MODULEID}/components/{COMPONENTID}?permanent=true", moduleId, id)
      .then()
      .statusCode(204);
    
    
    given().get("/modules/modules/{MODULEID}/components/{COMPONENTID}", moduleId, id)
      .then()
      .statusCode(404);
  }
}
