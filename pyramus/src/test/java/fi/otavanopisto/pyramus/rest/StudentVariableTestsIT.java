package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.VariableKey;
import fi.otavanopisto.pyramus.rest.model.VariableType;

public class StudentVariableTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentVariables() {
    VariableKey studentVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(studentVariable.getName()))
      .body("key", is(studentVariable.getKey()))
      .body("userEditable", is(studentVariable.getUserEditable()))
      .body("type", is(studentVariable.getType().toString()));

    given().headers(getAuthHeaders())
      .delete("/students/variables/{KEY}", studentVariable.getKey())
      .then()
      .statusCode(204);
  }

  @Test
  public void testListStudentVariables() {
    given().headers(getAuthHeaders())
      .get("/students/variables")
      .then()
      .statusCode(200)
      .body("id.size()", is(20))
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
  public void testFindStudentVariable() {
    given().headers(getAuthHeaders())
    .get("/students/variables/TV1")
    .then()
    .statusCode(200)
    .body("key", is("TV1"))
    .body("name", is("Test Variable #1 - text"))
    .body("type", is("TEXT"));
  }
  
  @Test
  public void testUpdateStudentVariable() {
    VariableKey studentVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(studentVariable.getName()))
      .body("key", is(studentVariable.getKey()))
      .body("userEditable", is(studentVariable.getUserEditable()))
      .body("type", is(studentVariable.getType().toString()));
    
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateVariable)
        .put("/students/variables/{KEY}", updateVariable.getKey())
        .then()
        .statusCode(200)
        .body("name", is(updateVariable.getName()))
        .body("key", is(updateVariable.getKey()))
        .body("userEditable", is(updateVariable.getUserEditable()))
        .body("type", is(updateVariable.getType().toString()));


    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/variables/{KEY}", studentVariable.getKey())
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentVariable() {
    VariableKey studentVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentVariable)
      .post("/students/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(studentVariable.getName()))
      .body("key", is(studentVariable.getKey()))
      .body("userEditable", is(studentVariable.getUserEditable()))
      .body("type", is(studentVariable.getType().toString()));
    
    given().headers(getAuthHeaders()).get("/students/variables/{KEY}", studentVariable.getKey())
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/variables/{KEY}", studentVariable.getKey())
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/variables/{KEY}", studentVariable.getKey())
      .then()
      .statusCode(404);
  }
}
