package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.PhoneNumber;

public class StudentPhoneNumberTestsIT extends AbstractRESTServiceTest {

  private final static long TEST_STUDENT_ID = 3l;

  @Test
  public void testCreateStudentPhoneNumber() {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/students/students/{ID}/phoneNumbers", TEST_STUDENT_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("number", is(phoneNumber.getNumber()))
      .body("contactTypeId", is(phoneNumber.getContactTypeId().intValue()))
      .body("defaultNumber", is( phoneNumber.getDefaultNumber()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStudentPhoneNumbers() {
    given().headers(getAuthHeaders())
      .get("/students/students/{ID}/phoneNumbers", TEST_STUDENT_ID)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(3) )
      .body("number[0]", is("+456 78 901 2345"))
      .body("contactTypeId[0]", is(1))
      .body("defaultNumber[0]", is(Boolean.TRUE));
  }
  
  @Test
  public void testFindStudentPhoneNumber() {
    given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, 3l)
      .then()
      .statusCode(200)
      .body("id", is(3) )
      .body("number", is("+456 78 901 2345"))
      .body("contactTypeId", is(1))
      .body("defaultNumber", is(Boolean.TRUE));
  }  

  @Test
  public void testUpdateStudentPhoneNumber() {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 1234 567");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/students/students/{ID}/phoneNumbers", TEST_STUDENT_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("number", is(phoneNumber.getNumber()))
      .body("contactTypeId", is(phoneNumber.getContactTypeId().intValue()))
      .body("defaultNumber", is( phoneNumber.getDefaultNumber()));
      
    long id = response.body().jsonPath().getLong("id");
    try {
      PhoneNumber updatedPhoneNumber = new PhoneNumber(id, 1l, Boolean.FALSE, "(888) 432 1098");
        
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updatedPhoneNumber)
        .put("/students/students/{ID}/phoneNumbers/{PHONENUMBERID}", TEST_STUDENT_ID, id)
        .then()
        .statusCode(200)
        .body("id", not(is((Long) null)))
        .body("number", is(updatedPhoneNumber.getNumber()))
        .body("contactTypeId", is(updatedPhoneNumber.getContactTypeId().intValue()))
        .body("defaultNumber", is(updatedPhoneNumber.getDefaultNumber()));
    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentPhoneNumber() {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/students/students/{STUDENTID}/phoneNumbers", TEST_STUDENT_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("number", is(phoneNumber.getNumber()))
      .body("contactTypeId", is(phoneNumber.getContactTypeId().intValue()))
      .body("defaultNumber", is( phoneNumber.getDefaultNumber()));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/phoneNumbers/{ID}", TEST_STUDENT_ID, id)
      .then()
      .statusCode(404);
  }
}
