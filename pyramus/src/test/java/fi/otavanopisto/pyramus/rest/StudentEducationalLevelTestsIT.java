package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.StudentEducationalLevel;

public class StudentEducationalLevelTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentEducationalLevel() {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studentEducationalLevel.getName()))
      .body("archived", is( studentEducationalLevel.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/educationalLevels/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listStudentEducationalLevels() {
    given().headers(getAuthHeaders())
      .get("/students/educationalLevels")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("StudentEducationalLevel #1" ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("StudentEducationalLevel #2" ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindStudentEducationalLevel() {
    given().headers(getAuthHeaders())
      .get("/students/educationalLevels/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("StudentEducationalLevel #1" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateStudentEducationalLevel() {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studentEducationalLevel.getName()))
      .body("archived", is( studentEducationalLevel.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentEducationalLevel updateStudentEducationalLevel = new StudentEducationalLevel(id, "Updated", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudentEducationalLevel)
        .put("/students/educationalLevels/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateStudentEducationalLevel.getId().intValue() ))
        .body("name", is(updateStudentEducationalLevel.getName()))
        .body("archived", is( updateStudentEducationalLevel.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/educationalLevels/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentEducationalLevel() {
    StudentEducationalLevel studentEducationalLevel = new StudentEducationalLevel(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentEducationalLevel)
      .post("/students/educationalLevels");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/educationalLevels/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/educationalLevels/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/educationalLevels/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/educationalLevels/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/educationalLevels/{ID}", id)
      .then()
      .statusCode(404);
  }
}
