package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit;

public class EducationalTimeUnitTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateEducationalTimeUnit() {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "create unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationalTimeUnit.getName()))
      .body("symbol", is(educationalTimeUnit.getSymbol()))
      .body("baseUnits", is(educationalTimeUnit.getBaseUnits().floatValue() ))
      .body("archived", is( educationalTimeUnit.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/common/educationalTimeUnits/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListEducationalTimeUnits() {
    given().headers(getAuthHeaders())
      .get("/common/educationalTimeUnits")
      .then()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[0]", is(1) )
      .body("name[0]", is("Hour"))
      .body("baseUnits[0]", is(1f))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Points"))
      .body("baseUnits[1]", is(1f))
      .body("archived[1]", is( false ))
      .body("id[2]", is(3) )
      .body("name[2]", is("Week"))
      .body("baseUnits[2]", is(168f))
      .body("archived[2]", is( false ));
  }
  
  @Test
  public void testFindEducationalTimeUnit() {
    given().headers(getAuthHeaders())
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
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationalTimeUnit.getName()))
      .body("symbol", is(educationalTimeUnit.getSymbol()))
      .body("baseUnits", is(educationalTimeUnit.getBaseUnits().floatValue() ))
      .body("archived", is( educationalTimeUnit.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    try {
      EducationalTimeUnit upOffsetDateTimeUnit = new EducationalTimeUnit(id, "updated unit", "sym", 2d, Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(upOffsetDateTimeUnit)
        .put("/common/educationalTimeUnits/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( upOffsetDateTimeUnit.getId().intValue() ))
        .body("name", is(upOffsetDateTimeUnit.getName()))
        .body("symbol", is(educationalTimeUnit.getSymbol()))
        .body("baseUnits", is(upOffsetDateTimeUnit.getBaseUnits().floatValue() ))
        .body("archived", is( upOffsetDateTimeUnit.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/common/educationalTimeUnits/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteEducationalTimeUnit() {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationalTimeUnit.getName()))
      .body("symbol", is(educationalTimeUnit.getSymbol()))
      .body("baseUnits", is(educationalTimeUnit.getBaseUnits().floatValue() ))
      .body("archived", is( educationalTimeUnit.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/common/educationalTimeUnits/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/educationalTimeUnits/{ID}", id)
      .then()
      .statusCode(404);
  }
}
