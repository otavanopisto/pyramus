package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactType;
import io.restassured.response.Response;

public class ContactTypePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateContactType(Role role) throws NoSuchFieldException {
    ContactType contactType = new ContactType(null, "create", Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");
    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_CONTACTTYPE, 200);
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/contactTypes/{ID}?permanent=true", id)
          .then();
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionslistContactTypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/contactTypes");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_CONTACTTYPES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindContactType(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/contactTypes/{ID}", 1);
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_CONTACTTYPE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateContactType(Role role) throws NoSuchFieldException {
    ContactType contactType = new ContactType(null, "Not Updated", Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");

    Long id = response.body().jsonPath().getLong("id");
    try {
      ContactType updateContactType = new ContactType(id, "Updated", Boolean.FALSE, Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateContactType)
        .put("/common/contactTypes/{ID}", id);
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_CONTACTTYPE, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/contactTypes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteContactType(Role role) throws NoSuchFieldException {
    ContactType contactType = new ContactType(null, "create type", Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");
    
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/common/contactTypes/{ID}", id);
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.DELETE_CONTACTTYPE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/common/contactTypes/{ID}?permanent=true", id);
  }
}
