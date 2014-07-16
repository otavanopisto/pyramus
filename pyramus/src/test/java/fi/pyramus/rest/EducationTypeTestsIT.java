package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.EducationType;

public class EducationTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateEducationType() {
    EducationType educationType = new EducationType(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationType.getName()))
      .body("code", is(educationType.getCode()))
      .body("archived", is( educationType.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/common/educationTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testFindEducationTypes() {
    given()
      .get("/common/educationTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Test Education Type" ))
      .body("code[0]", is("TEST"))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Test EduType 2" ))
      .body("code[1]", is("TST2"))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindEducationType() {
    given()
      .get("/common/educationTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Test Education Type" ))
      .body("code", is("TEST"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateEducationType() {
    EducationType educationType = new EducationType(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationType.getName()))
      .body("code", is(educationType.getCode()))
      .body("archived", is( educationType.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      EducationType updateEducationType = new EducationType(id, "Updated", "UPD", Boolean.FALSE);

      given()
        .contentType("application/json")
        .body(updateEducationType)
        .put("/common/educationTypes/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateEducationType.getId().intValue() ))
        .body("name", is(updateEducationType.getName()))
        .body("code", is(updateEducationType.getCode()))
        .body("archived", is( updateEducationType.getArchived() ));

    } finally {
      given()
        .delete("/common/educationTypes/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testEducationType() {
    EducationType educationType = new EducationType(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/common/educationTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/common/educationTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/common/educationTypes/{ID}", id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/common/educationTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().get("/common/educationTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
