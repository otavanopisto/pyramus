package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.EducationalTimeUnit;

public class EducationalTimeUnitTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateEducationalTimeUnit() {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "create unit", 1d, Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationalTimeUnit.getName()))
      .body("baseUnits", is(educationalTimeUnit.getBaseUnits().floatValue() ))
      .body("archived", is( educationalTimeUnit.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/common/educationalTimeUnits/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListEducationalTimeUnits() {
    given()
      .get("/common/educationalTimeUnits")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Hour"))
      .body("baseUnits[0]", is(1f))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Week"))
      .body("baseUnits[1]", is(168f))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindEducationalTimeUnit() {
    given()
      .get("/common/educationalTimeUnits/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Hour"))
      .body("baseUnits", is(1f))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateEducationalTimeUnit() {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", 1d, Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationalTimeUnit.getName()))
      .body("baseUnits", is(educationalTimeUnit.getBaseUnits().floatValue() ))
      .body("archived", is( educationalTimeUnit.getArchived() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      EducationalTimeUnit updateTimeUnit = new EducationalTimeUnit(id, "updated unit", 2d, Boolean.FALSE);

      given()
        .contentType("application/json")
        .body(updateTimeUnit)
        .put("/common/educationalTimeUnits/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateTimeUnit.getId().intValue() ))
        .body("name", is(updateTimeUnit.getName()))
        .body("baseUnits", is(updateTimeUnit.getBaseUnits().floatValue() ))
        .body("archived", is( updateTimeUnit.getArchived() ));

    } finally {
      given()
        .delete("/common/educationalTimeUnits/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteEducationalTimeUnit() {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", 1d, Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationalTimeUnit.getName()))
      .body("baseUnits", is(educationalTimeUnit.getBaseUnits().floatValue() ))
      .body("archived", is( educationalTimeUnit.getArchived() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/common/educationalTimeUnits/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().get("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(404);
  }
}
