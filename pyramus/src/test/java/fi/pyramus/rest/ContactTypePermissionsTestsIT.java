package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.CommonPermissions;
import fi.pyramus.rest.model.ContactType;

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
    ContactType contactType = new ContactType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");
    assertOk(response, commonPermissions, CommonPermissions.CREATE_CONTACTTYPE, 200);
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.equals(200)){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
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
    ContactType contactType = new ContactType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      ContactType updateContactType = new ContactType(id, "Updated", Boolean.FALSE);

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
    ContactType contactType = new ContactType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactType)
      .post("/common/contactTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/common/contactTypes/{ID}", id);
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_CONTACTTYPE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/common/contactTypes/{ID}?permanent=true", id);
  }
}
