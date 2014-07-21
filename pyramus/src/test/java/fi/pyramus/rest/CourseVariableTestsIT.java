package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.VariableKey;
import fi.pyramus.rest.model.VariableType;

public class CourseVariableTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseVariables() {
    VariableKey courseVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given()
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(courseVariable.getName()))
      .body("key", is(courseVariable.getKey()))
      .body("userEditable", is(courseVariable.getUserEditable()))
      .body("type", is(courseVariable.getType().toString()));

    given()
      .delete("/courses/variables/{KEY}", courseVariable.getKey())
      .then()
      .statusCode(204);
  }

  @Test
  public void testListCourseVariables() {
    given()
      .get("/courses/variables")
      .then()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("key[0]", is("TV1"))
      .body("name[0]", is("Test Variable #1 - text"))
      .body("type[0]", is("TEXT"))
      .body("key[1]", is("TV2"))
      .body("name[1]", is("Test Variable #2 - number"))
      .body("type[1]", is("NUMBER"))
      .body("key[2]", is("TV3"))
      .body("name[2]", is("Test Variable #3 - boolean"))
      .body("type[2]", is("BOOLEAN"));
  }
  
  @Test
  public void testFindCourseVariable() {
    given()
    .get("/courses/variables/TV1")
    .then()
    .statusCode(200)
    .body("key", is("TV1"))
    .body("name", is("Test Variable #1 - text"))
    .body("type", is("TEXT"));
  }
  
  @Test
  public void testUpdateCourseVariable() {
    VariableKey courseVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    Response response = given()
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(courseVariable.getName()))
      .body("key", is(courseVariable.getKey()))
      .body("userEditable", is(courseVariable.getUserEditable()))
      .body("type", is(courseVariable.getType().toString()));
    
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      given()
        .contentType("application/json")
        .body(updateVariable)
        .put("/courses/variables/{KEY}", updateVariable.getKey())
        .then()
        .statusCode(200)
        .body("name", is(updateVariable.getName()))
        .body("key", is(updateVariable.getKey()))
        .body("userEditable", is(updateVariable.getUserEditable()))
        .body("type", is(updateVariable.getType().toString()));


    } finally {
      given()
        .delete("/courses/variables/{KEY}", courseVariable.getKey())
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteCourseVariable() {
    VariableKey courseVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    Response response = given()
      .contentType("application/json")
      .body(courseVariable)
      .post("/courses/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(courseVariable.getName()))
      .body("key", is(courseVariable.getKey()))
      .body("userEditable", is(courseVariable.getUserEditable()))
      .body("type", is(courseVariable.getType().toString()));
    
    given().get("/courses/variables/{KEY}", courseVariable.getKey())
      .then()
      .statusCode(200);
    
    given()
      .delete("/courses/variables/{KEY}", courseVariable.getKey())
      .then()
      .statusCode(204);
    
    given().get("/courses/variables/{KEY}", courseVariable.getKey())
      .then()
      .statusCode(404);
  }
}
