package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactType;

@RunWith(Parameterized.class)
public class ContactTypePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public ContactTypePermissionsTestsIT(String role){
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateContactType() throws NoSuchFieldException {
    ContactType contactType = new ContactType(null, "create", Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");
    assertOk(response, commonPermissions, CommonPermissions.CREATE_CONTACTTYPE, 200);
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/contactTypes/{ID}?permanent=true", id)
          .then();
      }
    }
  }
  
  @Test
  public void testPermissionslistContactTypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/contactTypes");
    assertOk(response, commonPermissions, CommonPermissions.LIST_CONTACTTYPES, 200);
  }
  
  @Test
  public void testPermissionsFindContactType() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/contactTypes/{ID}", 1);
    assertOk(response, commonPermissions, CommonPermissions.FIND_CONTACTTYPE, 200);
  }
  
  @Test
  public void testPermissionsUpdateContactType() throws NoSuchFieldException {
    ContactType contactType = new ContactType(null, "Not Updated", Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");

    Long id = response.body().jsonPath().getLong("id");
    try {
      ContactType updateContactType = new ContactType(id, "Updated", Boolean.FALSE, Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateContactType)
        .put("/common/contactTypes/{ID}", id);
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_CONTACTTYPE, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/contactTypes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteContactType() throws NoSuchFieldException {
    ContactType contactType = new ContactType(null, "create type", Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");
    
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/common/contactTypes/{ID}", id);
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_CONTACTTYPE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/common/contactTypes/{ID}?permanent=true", id);
  }
}
