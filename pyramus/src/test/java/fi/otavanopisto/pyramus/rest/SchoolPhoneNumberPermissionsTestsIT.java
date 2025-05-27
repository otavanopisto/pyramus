package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.PhoneNumber;
import io.restassured.response.Response;

public class SchoolPhoneNumberPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSchoolPhoneNumber(Role role) throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAuthHeaders(role))
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
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSchoolPhoneNumbers(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{ID}/phoneNumbers", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLPHONENUMBERS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSchoolPhoneNumber(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLPHONENUMBER, 200);
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSchoolPhoneNumber(Role role) throws NoSuchFieldException {
    PhoneNumber phoneNumber = new PhoneNumber(null, 1l, Boolean.FALSE, "(123) 12 234 5678");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(phoneNumber)
      .post("/schools/schools/{SCHOOLID}/phoneNumbers", 1l);
      
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id);
    assertOk(role, deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLPHONENUMBER, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{SCHOOLID}/phoneNumbers/{ID}", 1l, id);
  }
}
