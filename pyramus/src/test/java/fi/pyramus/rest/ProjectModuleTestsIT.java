package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.ProjectModule;
import fi.pyramus.rest.model.ProjectModuleOptionality;

public class ProjectModuleTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateProjectModule() {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given()
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("moduleId", is(projectModule.getModuleId().intValue()))
      .body("optionality", is(projectModule.getOptionality().toString() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListProjectModules() {
    given()
      .get("/projects/projects/{PROJECTID}/modules", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("moduleId[0]", is(1))
      .body("optionality[0]", is("OPTIONAL"));
  }

  @Test
  public void testFindProjectModule() {
    given()
      .get("/projects/projects/{PROJECTID}/modules/{ID}", 1l, 1l)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("moduleId", is(1))
      .body("optionality", is("OPTIONAL"));
  }
  
  @Test
  public void testUpdateProjectModule() {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given()
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("moduleId", is(projectModule.getModuleId().intValue()))
      .body("optionality", is(projectModule.getOptionality().toString() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      ProjectModule updateProjectModule = new ProjectModule(id, 1l, ProjectModuleOptionality.MANDATORY);
      
      given()
        .contentType("application/json")
        .body(updateProjectModule)
        .put("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id)
        .then()
        .statusCode(200)
        .body("id", not(is((Long) null)))
        .body("moduleId", is(updateProjectModule.getModuleId().intValue()))
        .body("optionality", is(updateProjectModule.getOptionality().toString() ));

    } finally {
      given()
        .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteProjectModule() {
    ProjectModule projectModule = new ProjectModule(null, 1l, ProjectModuleOptionality.OPTIONAL);
    
    Response response = given()
      .contentType("application/json")
      .body(projectModule)
      .post("/projects/projects/{PROJECTID}/modules", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("moduleId", is(projectModule.getModuleId().intValue()))
      .body("optionality", is(projectModule.getOptionality().toString() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    given().get("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id)
      .then()
      .statusCode(204);
    
    given().get("/projects/projects/{PROJECTID}/modules/{ID}", 1l, id)
      .then()
      .statusCode(404);
  }
}
