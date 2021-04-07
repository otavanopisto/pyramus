package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;
import fi.otavanopisto.pyramus.rest.model.BillingDetails;
import fi.otavanopisto.pyramus.rest.model.Organization;

public class OrganizationTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateOrganization() {
    BillingDetails billingDetails = new BillingDetails();
    Organization organization = new Organization(null, "create", billingDetails, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(organization)
      .post("/organizations");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(organization.getName()))
      .body("archived", is(organization.getArchived()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/organizations/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listOrganizations() {
    given().headers(getAuthHeaders())
      .get("/organizations")
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("name[0]", is("Common Test Organization"))
      .body("archived[0]", is(false));
  }
  
  @Test
  public void testFindOrganization() {
    given().headers(getAuthHeaders())
      .get("/organizations/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Common Test Organization"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateOrganization() {
    BillingDetails billingDetails = new BillingDetails();
    Organization organization = new Organization(null, "Not Updated", billingDetails, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(organization)
      .post("/organizations");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(organization.getName()))
      .body("archived", is(organization.getArchived()));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Organization updateOrganization = new Organization(id, "Updated", billingDetails, Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateOrganization)
        .put("/organizations/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateOrganization.getId().intValue()))
        .body("name", is(updateOrganization.getName()))
        .body("archived", is(updateOrganization.getArchived()));
    } finally {
      given().headers(getAuthHeaders())
        .delete("/organizations/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteOrganization() {
    BillingDetails billingDetails = new BillingDetails();
    Organization organization = new Organization(null, "create type", billingDetails, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(organization)
      .post("/organizations");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders())
      .get("/organizations/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/organizations/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/organizations/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/organizations/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/organizations/{ID}", id)
      .then()
      .statusCode(404);
  }
}
