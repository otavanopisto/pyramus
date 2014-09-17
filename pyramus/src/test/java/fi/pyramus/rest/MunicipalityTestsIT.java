package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Municipality;

public class MunicipalityTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateMunicipality() {
    Municipality municipality = new Municipality(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(municipality.getName()))
      .body("code", is(municipality.getCode()))
      .body("archived", is( municipality.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/municipalities/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listLangauegs() {
    given().headers(getAuthHeaders())
      .get("/students/municipalities")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Municipality #1" ))
      .body("code[0]", is("TST1"))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Municipality #2" ))
      .body("code[1]", is("TST2"))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindMunicipality() {
    given().headers(getAuthHeaders())
      .get("/students/municipalities/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Municipality #1" ))
      .body("code", is("TST1"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateMunicipality() {
    Municipality municipality = new Municipality(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(municipality.getName()))
      .body("code", is(municipality.getCode()))
      .body("archived", is( municipality.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Municipality updateMunicipality = new Municipality(id, "Updated", "UPD", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateMunicipality)
        .put("/students/municipalities/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateMunicipality.getId().intValue() ))
        .body("name", is(updateMunicipality.getName()))
        .body("code", is(updateMunicipality.getCode()))
        .body("archived", is( updateMunicipality.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/municipalities/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteMunicipality() {
    Municipality municipality = new Municipality(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/municipalities/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/municipalities/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/municipalities/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/municipalities/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/municipalities/{ID}", id)
      .then()
      .statusCode(404);
  }
}
