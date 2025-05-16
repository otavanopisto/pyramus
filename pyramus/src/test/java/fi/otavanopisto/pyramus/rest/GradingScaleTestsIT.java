package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.GradingScale;

public class GradingScaleTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateGradingScale() {
    GradingScale gradingScale = new GradingScale(null, "create scale", "grading scale for testing creation", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(gradingScale)
      .post("/common/gradingScales");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(gradingScale.getName()))
      .body("description", is(gradingScale.getDescription()))
      .body("archived", is( gradingScale.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/common/gradingScales/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListGradingScales() {
    given().headers(getAuthHeaders())
      .get("/common/gradingScales")
      .then()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[0]", is(1) )
      .body("name[0]", is("test scale #1" ))
      .body("description[0]", is("grading scale for testing #1"))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("test scale #2" ))
      .body("description[1]", is("grading scale for testing #2"))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindGradingScale() {
    given().headers(getAuthHeaders())
      .get("/common/gradingScales/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("test scale #1" ))
      .body("description", is("grading scale for testing #1"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateGradingScale() {
    GradingScale gradingScale = new GradingScale(null, "not updated", "grading scale has not been updated yet", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(gradingScale)
      .post("/common/gradingScales");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(gradingScale.getName()))
      .body("description", is(gradingScale.getDescription()))
      .body("archived", is( gradingScale.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    try {
      GradingScale updateScale = new GradingScale(id, "updated", "grading scale has been updated", Boolean.FALSE);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateScale)
        .put("/common/gradingScales/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateScale.getId().intValue() ))
        .body("name", is(updateScale.getName()))
        .body("description", is(updateScale.getDescription()))
        .body("archived", is( updateScale.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/common/gradingScales/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteGradingScale() {
    GradingScale gradingScale = new GradingScale(null, "to be deleted", "grading scale to be deleted", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(gradingScale)
      .post("/common/gradingScales");
    
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/common/gradingScales/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/common/gradingScales/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/gradingScales/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/common/gradingScales/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/gradingScales/{ID}", id)
      .then()
      .statusCode(404);
  }
}
