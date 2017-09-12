package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Address;

public class StaffMemberAddressTestsIT extends AbstractRESTServiceTest {

  private final static long TEST_STAFFMEMBER_ID = 6l;
  
  @Test
  public void testCreateStaffMemberAddress() {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/staff/members/{ID}/addresses", TEST_STAFFMEMBER_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(address.getName()))
      .body("streetAddress", is(address.getStreetAddress()))
      .body("postalCode", is(address.getPostalCode()))
      .body("city", is(address.getCity()))
      .body("country", is(address.getCountry()))
      .body("contactTypeId", is(address.getContactTypeId().intValue()))
      .body("defaultAddress", is( address.getDefaultAddress()));
    
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/staff/members/{STAFFMEMBERID}/addresses/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStaffMemberAddresses() {
    given().headers(getAuthHeaders())
      .get("/staff/members/{ID}/addresses", TEST_STAFFMEMBER_ID)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(6) )
      .body("name[0]", is((String) null))
      .body("streetAddress[0]", is("456 Mountains"))
      .body("postalCode[0]", is("1111"))
      .body("city[0]", is("Chester"))
      .body("country[0]", is("Belgium"))
      .body("contactTypeId[0]", is(1))
      .body("defaultAddress[0]", is(Boolean.TRUE));
  }
  
  @Test
  public void testFindStaffMemberAddress() {
    given().headers(getAuthHeaders())
      .get("/staff/members/{STAFFMEMBERID}/addresses/{ID}", TEST_STAFFMEMBER_ID, 6l)
      .then()
      .statusCode(200)
      .body("id", is(6) )
      .body("name", is((String) null))
      .body("streetAddress", is("456 Mountains"))
      .body("postalCode", is("1111"))
      .body("city", is("Chester"))
      .body("country", is("Belgium"))
      .body("contactTypeId", is(1))
      .body("defaultAddress", is(Boolean.TRUE));
  }  

  @Test
  public void testDeleteStaffMemberAddress() {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/staff/members/{STAFFMEMBERID}/addresses", TEST_STAFFMEMBER_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(address.getName()))
      .body("streetAddress", is(address.getStreetAddress()))
      .body("postalCode", is(address.getPostalCode()))
      .body("city", is(address.getCity()))
      .body("country", is(address.getCountry()))
      .body("contactTypeId", is(address.getContactTypeId().intValue()))
      .body("defaultAddress", is( address.getDefaultAddress()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/staff/members/{STAFFMEMBERID}/addresses/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/staff/members/{STAFFMEMBERID}/addresses/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/staff/members/{STAFFMEMBERID}/addresses/{ID}", TEST_STAFFMEMBER_ID, id)
      .then()
      .statusCode(404);
  }
}
