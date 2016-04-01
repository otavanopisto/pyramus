package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.PhoneNumber;

public class StaffMemberPhoneNumberTestsIT extends AbstractRESTServiceTest {

  private final static long TEST_STAFFMEMBER_ID = 6l;

  @Test
  public void testCreateStudentPhoneNumber() {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/staff/members/{ID}/phoneNumbers", TEST_STAFFMEMBER_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("number", is(phoneNumber.getNumber()))
      .body("contactTypeId", is(phoneNumber.getContactTypeId().intValue()))
      .body("defaultNumber", is( phoneNumber.getDefaultNumber()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/staff/members/{STUDENTID}/phoneNumbers/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStudentPhoneNumbers() {
    given().headers(getAuthHeaders())
      .get("/staff/members/{ID}/phoneNumbers", TEST_STAFFMEMBER_ID)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(6) )
      .body("number[0]", is("+567 89 012 3458"))
      .body("contactTypeId[0]", is(1))
      .body("defaultNumber[0]", is(Boolean.TRUE));
  }
  
  @Test
  public void testFindStudentPhoneNumber() {
    given().headers(getAuthHeaders())
      .get("/staff/members/{STUDENTID}/phoneNumbers/{ID}", TEST_STAFFMEMBER_ID, 6l)
      .then()
      .statusCode(200)
      .body("id", is(6) )
      .body("number", is("+567 89 012 3458"))
      .body("contactTypeId", is(1))
      .body("defaultNumber", is(Boolean.TRUE));
  }  

  @Test
  public void testDeleteStudentPhoneNumber() {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/staff/members/{STUDENTID}/phoneNumbers", TEST_STAFFMEMBER_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("number", is(phoneNumber.getNumber()))
      .body("contactTypeId", is(phoneNumber.getContactTypeId().intValue()))
      .body("defaultNumber", is( phoneNumber.getDefaultNumber()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/staff/members/{STUDENTID}/phoneNumbers/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/staff/members/{STUDENTID}/phoneNumbers/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/staff/members/{STUDENTID}/phoneNumbers/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(404);
  }
}
