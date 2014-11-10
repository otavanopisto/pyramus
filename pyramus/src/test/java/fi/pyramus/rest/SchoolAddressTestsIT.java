package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Address;

public class SchoolAddressTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateSchoolAddress() {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/schools/schools/{ID}/addresses", 1l);

    response.then()
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
      .delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListSchoolAddresses() {
    given().headers(getAuthHeaders())
      .get("/schools/schools/{ID}/addresses", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("name[0]", is((String) null))
      .body("streetAddress[0]", is("2636 Johnston Harbors"))
      .body("postalCode[0]", is("76763-3962"))
      .body("city[0]", is("Eastbury"))
      .body("country[0]", is("Senegal"))
      .body("defaultAddress[0]", is(Boolean.TRUE));
  }
  
  @Test
  public void testFindSchoolAddress() {
    given().headers(getAuthHeaders())
      .get("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, 1l)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is((String) null))
      .body("streetAddress", is("2636 Johnston Harbors"))
      .body("postalCode", is("76763-3962"))
      .body("city", is("Eastbury"))
      .body("country", is("Senegal"))
      .body("contactTypeId", is(1))
      .body("defaultAddress", is(Boolean.TRUE));
  }  

  @Test
  public void testDeleteSchoolAddress() {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/schools/schools/{SCHOOLID}/addresses", 1l);

    response.then()
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
    
    given().headers(getAuthHeaders()).get("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(404);
  }
}
