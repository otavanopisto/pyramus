package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Language;

public class LanguageTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateLanguage() {
    Language language = new Language(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(language.getName()))
      .body("code", is(language.getCode()))
      .body("archived", is( language.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/languages/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listLanguages() {
    given().headers(getAuthHeaders())
      .get("/students/languages")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Language #1" ))
      .body("code[0]", is("TST1"))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Language #2" ))
      .body("code[1]", is("TST2"))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindLanguage() {
    given().headers(getAuthHeaders())
      .get("/students/languages/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Language #1" ))
      .body("code", is("TST1"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateLanguage() {
    Language language = new Language(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(language.getName()))
      .body("code", is(language.getCode()))
      .body("archived", is( language.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Language updateLanguage = new Language(id, "Updated", "UPD", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateLanguage)
        .put("/students/languages/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateLanguage.getId().intValue() ))
        .body("name", is(updateLanguage.getName()))
        .body("code", is(updateLanguage.getCode()))
        .body("archived", is( updateLanguage.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/languages/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteLanguage() {
    Language language = new Language(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/languages/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/languages/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/languages/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/languages/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/languages/{ID}", id)
      .then()
      .statusCode(404);
  }
}
