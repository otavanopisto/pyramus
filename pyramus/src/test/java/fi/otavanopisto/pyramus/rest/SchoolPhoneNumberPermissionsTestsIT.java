package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.PhoneNumber;

@RunWith(Parameterized.class)
public class SchoolPhoneNumberPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public SchoolPhoneNumberPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSchoolPhoneNumber() throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/schools/schools/{ID}/phoneNumbers", 1l);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id);
      }
    }
  }
  
  @Test
  public void testPermissionsListSchoolPhoneNumbers() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{ID}/phoneNumbers", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLPHONENUMBERS, 200);
  }
  
  @Test
  public void testPermissionsFindSchoolPhoneNumber() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLPHONENUMBER, 200);
  }  

  @Test
  public void testPermissionsDeleteSchoolPhoneNumber() throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/schools/schools/{SCHOOLID}/phoneNumbers", 1l);
      
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id);
    assertOk(deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLPHONENUMBER, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id);
  }
}
