package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.ContactType;

public class ContactTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateContactType() {
    ContactType contactType = new ContactType(null, "create", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(contactType.getName()))
      .body("archived", is( contactType.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/common/contactTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listContactTypes() {
    given()
      .get("/common/contactTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("name[0]", is("Home" ))
      .body("archived[0]", is( false ));
  }
  
  @Test
  public void testFindContactType() {
    given()
      .get("/common/contactTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Home" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateContactType() {
    ContactType contactType = new ContactType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(contactType.getName()))
      .body("archived", is( contactType.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      ContactType updateContactType = new ContactType(id, "Updated", Boolean.FALSE);

      given()
        .contentType("application/json")
        .body(updateContactType)
        .put("/common/contactTypes/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateContactType.getId().intValue() ))
        .body("name", is(updateContactType.getName()))
        .body("archived", is( updateContactType.getArchived() ));

    } finally {
      given()
        .delete("/common/contactTypes/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteContactType() {
    ContactType contactType = new ContactType(null, "create type", Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/common/contactTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/common/contactTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/common/contactTypes/{ID}", id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/common/contactTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().get("/common/contactTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
