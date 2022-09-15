package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.ContactURLType;

public class ContactURLTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateContactURLType() {
    ContactURLType contactURLType = new ContactURLType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(contactURLType.getName()))
      .body("archived", is( contactURLType.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/common/contactURLTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listContactURLTypes() {
    given().headers(getAuthHeaders())
      .get("/common/contactURLTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("name[0]", is("WWW" ))
      .body("archived[0]", is( false ));
  }
  
  @Test
  public void testFindContactURLType() {
    given().headers(getAuthHeaders())
      .get("/common/contactURLTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("WWW" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateContactURLType() {
    ContactURLType contactURLType = new ContactURLType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(contactURLType.getName()))
      .body("archived", is( contactURLType.getArchived() ));
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      ContactURLType updateContactURLType = new ContactURLType(id, "Updated", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateContactURLType)
        .put("/common/contactURLTypes/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateContactURLType.getId().intValue() ))
        .body("name", is(updateContactURLType.getName()))
        .body("archived", is( updateContactURLType.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/common/contactURLTypes/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteContactURLType() {
    ContactURLType contactURLType = new ContactURLType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");
    
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/common/contactURLTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/common/contactURLTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/contactURLTypes/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/common/contactURLTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/contactURLTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
