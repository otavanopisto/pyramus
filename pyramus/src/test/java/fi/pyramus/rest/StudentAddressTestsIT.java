package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Address;

public class StudentAddressTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentAddress() {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/students/students/{ID}/addresses", 1l);

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
      .delete("/students/students/{STUDENTID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStudentAddresses() {
    given().headers(getAuthHeaders())
      .get("/students/students/{ID}/addresses", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(3) )
      .body("name[0]", is((String) null))
      .body("streetAddress[0]", is("6967 Bailee Mission"))
      .body("postalCode[0]", is("17298"))
      .body("city[0]", is("Southshire"))
      .body("country[0]", is("Yemen"))
      .body("defaultAddress[0]", is(Boolean.TRUE));
  }
  
  @Test
  public void testFindStudentAddress() {
    given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/addresses/{ID}", 1l, 3l)
      .then()
      .statusCode(200)
      .body("id", is(3) )
      .body("name", is((String) null))
      .body("streetAddress", is("6967 Bailee Mission"))
      .body("postalCode", is("17298"))
      .body("city", is("Southshire"))
      .body("country", is("Yemen"))
      .body("contactTypeId", is(1))
      .body("defaultAddress", is(Boolean.TRUE));
  }  

  @Test
  public void testDeleteStudentAddress() {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/students/students/{STUDENTID}/addresses", 1l);

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
    
    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/addresses/{ID}", 1l, id)
      .then()
      .statusCode(404);
  }
}
