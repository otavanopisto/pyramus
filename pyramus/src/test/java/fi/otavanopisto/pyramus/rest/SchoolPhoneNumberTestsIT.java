package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.PhoneNumber;

public class SchoolPhoneNumberTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateSchoolPhoneNumber() {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/schools/schools/{ID}/phoneNumbers", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("number", is(phoneNumber.getNumber()))
      .body("contactTypeId", is(phoneNumber.getContactTypeId().intValue()))
      .body("defaultNumber", is( phoneNumber.getDefaultNumber()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListSchoolPhoneNumbers() {
    given().headers(getAuthHeaders())
      .get("/schools/schools/{ID}/phoneNumbers", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("number[0]", is("+123 45 678 9012"))
      .body("contactTypeId[0]", is(1))
      .body("defaultNumber[0]", is(Boolean.TRUE));
  }
  
  @Test
  public void testFindSchoolPhoneNumber() {
    given().headers(getAuthHeaders())
      .get("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, 1l)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("number", is("+123 45 678 9012"))
      .body("contactTypeId", is(1))
      .body("defaultNumber", is(Boolean.TRUE));
  }  

  @Test
  public void testDeleteSchoolPhoneNumber() {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/schools/schools/{SCHOOLID}/phoneNumbers", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("number", is(phoneNumber.getNumber()))
      .body("contactTypeId", is(phoneNumber.getContactTypeId().intValue()))
      .body("defaultNumber", is( phoneNumber.getDefaultNumber()));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id)
      .then()
      .statusCode(404);
  }
}
