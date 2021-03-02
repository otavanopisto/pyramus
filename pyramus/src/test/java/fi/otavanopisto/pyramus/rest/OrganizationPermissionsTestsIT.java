package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fi.otavanopisto.pyramus.rest.model.BillingDetails;
import fi.otavanopisto.pyramus.rest.model.Organization;
import fi.otavanopisto.pyramus.security.impl.permissions.OrganizationPermissions;
import io.restassured.response.Response;

@RunWith(Parameterized.class)
public class OrganizationPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private OrganizationPermissions organizationPermissions = new OrganizationPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public OrganizationPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateOrganization() throws NoSuchFieldException {
    BillingDetails billingDetails = new BillingDetails();
    Organization organization = new Organization(null, getClass().getSimpleName(), billingDetails, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(organization)
      .post("/organizations");

    assertOk(response, organizationPermissions, OrganizationPermissions.CREATE_ORGANIZATION, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given()
          .headers(getAdminAuthHeaders())
          .delete("/organizations/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListOrganizations() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/organizations");
    assertOk(response, organizationPermissions, OrganizationPermissions.LIST_ORGANIZATIONS, 200);
  }
  
  @Test
  public void testPermissionsFindOrganization() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/organizations/{ID}", 1);
    assertOk(response, organizationPermissions, OrganizationPermissions.FIND_ORGANIZATION, 200);
  }
  
  @Test
  public void testPermissionsUpdateOrganization() throws NoSuchFieldException {
    BillingDetails billingDetails = new BillingDetails();
    Organization organizations = new Organization(null, "Original Organization", billingDetails, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(organizations)
      .post("/organizations");

    Long id = response.body().jsonPath().getLong("id");
    try {
      BillingDetails updatedBillingDetails = new BillingDetails();
      Organization updateOrganization = new Organization(id, "Updated Organization", updatedBillingDetails, Boolean.FALSE);

      Response updateResponse = given()
          .headers(getAuthHeaders())
          .contentType("application/json")
          .body(updateOrganization)
          .put("/organizations/{ID}", id);
      
      assertOk(updateResponse, organizationPermissions, OrganizationPermissions.UPDATE_ORGANIZATION, 200);
    } finally {
      given()
        .headers(getAdminAuthHeaders())
        .delete("/organizations/{ID}?permanent=true", id)
        .then();
    }
  }
  
  @Test
  public void testPermissionsDeleteOrganization() throws NoSuchFieldException {
    BillingDetails billingDetails = new BillingDetails();
    Organization organization = new Organization(null, "Organization to be deleted", billingDetails, Boolean.FALSE);
    
    Response response = given()
        .headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(organization)
        .post("/organizations");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      Response deleteResponse = given().headers(getAuthHeaders())
        .delete("/organizations/{ID}", id);
      
      assertOk(deleteResponse, organizationPermissions, OrganizationPermissions.DELETE_ORGANIZATION, 204);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/organizations/{ID}?permanent=true", id);
    }
  }

}
