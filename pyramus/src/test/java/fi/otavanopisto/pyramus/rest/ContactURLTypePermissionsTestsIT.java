package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactURLType;
import io.restassured.response.Response;

public class ContactURLTypePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateContactURLType(Role role) throws NoSuchFieldException {
    ContactURLType contactURLType = new ContactURLType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");
    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_CONTACTTYPE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/contactURLTypes/{ID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListContactURLTypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/contactURLTypes");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_CONTACTURLTYPES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindContactURLType(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/contactURLTypes/{ID}", 1);
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_CONTACTURLTYPE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateContactURLType(Role role) throws NoSuchFieldException {
    ContactURLType contactURLType = new ContactURLType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      ContactURLType updateContactURLType = new ContactURLType(id, "Updated", Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateContactURLType)
        .put("/common/contactURLTypes/{ID}", id);
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_CONTACTURLTYPE, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/contactURLTypes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteContactURLType(Role role) throws NoSuchFieldException {
    ContactURLType contactURLType = new ContactURLType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURLType)
      .post("/common/contactURLTypes");
    
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/common/contactURLTypes/{ID}", id);
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.DELETE_CONTACTURLTYPE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/common/contactURLTypes/{ID}?permanent=true", id);
  }
}
