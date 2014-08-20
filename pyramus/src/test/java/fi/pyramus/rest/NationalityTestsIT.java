package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Nationality;

public class NationalityTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateNationality() {
    Nationality nationality = new Nationality(null, "TST", "create", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(nationality.getName()))
      .body("code", is(nationality.getCode()))
      .body("archived", is( nationality.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/students/nationalities/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listLangauegs() {
    given()
      .get("/students/nationalities")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Nationality #1" ))
      .body("code[0]", is("TST1"))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Nationality #2" ))
      .body("code[1]", is("TST2"))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindNationality() {
    given()
      .get("/students/nationalities/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Nationality #1" ))
      .body("code", is("TST1"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateNationality() {
    Nationality nationality = new Nationality(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(nationality.getName()))
      .body("code", is(nationality.getCode()))
      .body("archived", is( nationality.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Nationality updateNationality = new Nationality(id, "Updated", "UPD", Boolean.FALSE);

      given()
        .contentType("application/json")
        .body(updateNationality)
        .put("/students/nationalities/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateNationality.getId().intValue() ))
        .body("name", is(updateNationality.getName()))
        .body("code", is(updateNationality.getCode()))
        .body("archived", is( updateNationality.getArchived() ));

    } finally {
      given()
        .delete("/students/nationalities/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteNationality() {
    Nationality nationality = new Nationality(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/students/nationalities/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/students/nationalities/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/students/nationalities/{ID}", id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/students/nationalities/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().get("/students/nationalities/{ID}", id)
      .then()
      .statusCode(404);
  }
}
