package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.Address;

@RunWith(Parameterized.class)
public class SchoolAddressPermissionsTestsIT extends AbstractRESTPermissionsTest {
  
  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
    
  public SchoolAddressPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSchoolAddress() throws NoSuchFieldException {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/schools/schools/{ID}/addresses", 1l);
    assertOk(response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLADDRESS, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id);
      }
    }
  }
  
  @Test
  public void testPermissionsListSchoolAddresses() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
        .get("/schools/schools/{ID}/addresses", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLADDRESSS, 200);
  }
  
  @Test
  public void testPermissionsFindSchoolAddress() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLADDRESS, 200);
  }  

  @Test
  public void testPermissionsDeleteSchoolAddress() throws NoSuchFieldException {
    Address address = new Address(null, 1l, Boolean.FALSE, "Caleb Great", "24916 Nicole Land", "59903-2455", "Porthaven", "Uruguay");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(address)
      .post("/schools/schools/{SCHOOLID}/addresses", 1l);
      
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id);
    assertOk(deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLADDRESS, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders()).delete("/schools/schools/{SCHOOLID}/addresses/{ID}", 1l, id);
  }
}
