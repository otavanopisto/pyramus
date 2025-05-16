package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.model.BillingDetails;
import fi.otavanopisto.pyramus.rest.model.Organization;
import fi.otavanopisto.pyramus.security.impl.permissions.OrganizationPermissions;
import io.restassured.response.Response;

public class OrganizationPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private OrganizationPermissions organizationPermissions = new OrganizationPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateOrganization(Role role) throws NoSuchFieldException {
    BillingDetails billingDetails = new BillingDetails();
    Organization organization = new Organization(null, getClass().getSimpleName(), billingDetails, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(organization)
      .post("/organizations");

    assertOk(role, response, organizationPermissions, OrganizationPermissions.CREATE_ORGANIZATION, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given()
          .headers(getAdminAuthHeaders())
          .delete("/organizations/{ID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListOrganizations(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/organizations");
    assertOk(role, response, organizationPermissions, OrganizationPermissions.LIST_ORGANIZATIONS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindOrganization(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/organizations/{ID}", 1);
    assertOk(role, response, organizationPermissions, OrganizationPermissions.FIND_ORGANIZATION, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateOrganization(Role role) throws NoSuchFieldException {
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
          .headers(getAuthHeaders(role))
          .contentType("application/json")
          .body(updateOrganization)
          .put("/organizations/{ID}", id);
      
      assertOk(role, updateResponse, organizationPermissions, OrganizationPermissions.UPDATE_ORGANIZATION, 200);
    } finally {
      given()
        .headers(getAdminAuthHeaders())
        .delete("/organizations/{ID}?permanent=true", id)
        .then();
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteOrganization(Role role) throws NoSuchFieldException {
    BillingDetails billingDetails = new BillingDetails();
    Organization organization = new Organization(null, "Organization to be deleted", billingDetails, Boolean.FALSE);
    
    Response response = given()
        .headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(organization)
        .post("/organizations");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      Response deleteResponse = given().headers(getAuthHeaders(role))
        .delete("/organizations/{ID}", id);
      
      assertOk(role, deleteResponse, organizationPermissions, OrganizationPermissions.DELETE_ORGANIZATION, 204);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/organizations/{ID}?permanent=true", id);
    }
  }

}
