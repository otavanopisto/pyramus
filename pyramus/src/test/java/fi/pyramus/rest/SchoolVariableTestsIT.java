package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.VariableKey;
import fi.pyramus.rest.model.VariableType;

public class SchoolVariableTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateSchoolVariables() {
    VariableKey schoolVariable = new VariableKey("crevar", "variable to be created", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(schoolVariable.getName()))
      .body("key", is(schoolVariable.getKey()))
      .body("userEditable", is(schoolVariable.getUserEditable()))
      .body("type", is(schoolVariable.getType().toString()));

    given().headers(getAuthHeaders())
      .delete("/schools/variables/{KEY}", schoolVariable.getKey())
      .then()
      .statusCode(204);
  }

  @Test
  public void testListSchoolVariables() {
    given().headers(getAuthHeaders())
      .get("/schools/variables")
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
  public void testFindSchoolVariable() {
    given().headers(getAuthHeaders())
    .get("/schools/variables/TV1")
    .then()
    .statusCode(200)
    .body("key", is("TV1"))
    .body("name", is("Test Variable #1 - text"))
    .body("type", is("TEXT"));
  }
  
  @Test
  public void testUpdateSchoolVariable() {
    VariableKey schoolVariable = new VariableKey("upd", "not updated", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(schoolVariable.getName()))
      .body("key", is(schoolVariable.getKey()))
      .body("userEditable", is(schoolVariable.getUserEditable()))
      .body("type", is(schoolVariable.getType().toString()));
    
    try {
      VariableKey updateVariable = new VariableKey("upd", "updated", true, VariableType.NUMBER);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateVariable)
        .put("/schools/variables/{KEY}", updateVariable.getKey())
        .then()
        .statusCode(200)
        .body("name", is(updateVariable.getName()))
        .body("key", is(updateVariable.getKey()))
        .body("userEditable", is(updateVariable.getUserEditable()))
        .body("type", is(updateVariable.getType().toString()));


    } finally {
      given().headers(getAuthHeaders())
        .delete("/schools/variables/{KEY}", schoolVariable.getKey())
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteSchoolVariable() {
    VariableKey schoolVariable = new VariableKey("delete", "variable to be deleted", false, VariableType.TEXT);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolVariable)
      .post("/schools/variables");
    
    response.then()
      .statusCode(200)
      .body("name", is(schoolVariable.getName()))
      .body("key", is(schoolVariable.getKey()))
      .body("userEditable", is(schoolVariable.getUserEditable()))
      .body("type", is(schoolVariable.getType().toString()));
    
    given().headers(getAuthHeaders()).get("/schools/variables/{KEY}", schoolVariable.getKey())
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/schools/variables/{KEY}", schoolVariable.getKey())
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/schools/variables/{KEY}", schoolVariable.getKey())
      .then()
      .statusCode(404);
  }
}
