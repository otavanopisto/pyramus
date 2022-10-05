package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactURLType;

@RunWith(Parameterized.class)
public class ContactURLTypePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public ContactURLTypePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateContactURLType() throws NoSuchFieldException {
    ContactURLType contactURLType = new ContactURLType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");
    assertOk(response, commonPermissions, CommonPermissions.CREATE_CONTACTTYPE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/contactURLTypes/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListContactURLTypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/contactURLTypes");
    assertOk(response, commonPermissions, CommonPermissions.LIST_CONTACTURLTYPES, 200);
  }
  
  @Test
  public void testPermissionsFindContactURLType() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/contactURLTypes/{ID}", 1);
    assertOk(response, commonPermissions, CommonPermissions.FIND_CONTACTURLTYPE, 200);
  }
  
  @Test
  public void testUpdateContactURLType() throws NoSuchFieldException {
    ContactURLType contactURLType = new ContactURLType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      ContactURLType updateContactURLType = new ContactURLType(id, "Updated", Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateContactURLType)
        .put("/common/contactURLTypes/{ID}", id);
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_CONTACTURLTYPE, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/contactURLTypes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteContactURLType() throws NoSuchFieldException {
    ContactURLType contactURLType = new ContactURLType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");
    
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/common/contactURLTypes/{ID}", id);
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_CONTACTURLTYPE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/common/contactURLTypes/{ID}?permanent=true", id);
  }
}
