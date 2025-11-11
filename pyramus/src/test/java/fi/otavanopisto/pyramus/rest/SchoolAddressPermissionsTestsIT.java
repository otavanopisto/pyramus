package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.Address;
import io.restassured.response.Response;

public class SchoolAddressPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSchoolAddress(Role role) throws NoSuchFieldException {
    Address address = new Address(null, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(address)
      .post("/schools/schools/{ID}/addresses", 1l);
    assertOk(role, response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLADDRESS, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSchoolAddresses(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
        .get("/schools/schools/{ID}/addresses", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLADDRESSS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSchoolAddress(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLADDRESS, 200);
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSchoolAddress(Role role) throws NoSuchFieldException {
    Address address = new Address(null, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/schools/schools/{SCHOOLID}/addresses", 1l);
      
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id);
    assertOk(role, deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLADDRESS, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id);
  }
}
