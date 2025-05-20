package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactURL;
import io.restassured.response.Response;

public class SchoolContactURLPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSchoolContactURL(Role role) throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(contactURL)
      .post("/schools/schools/{ID}/contactURLs", 1l);

    assertOk(role, response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLCONTACTURL, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSchoolContactURLs(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{ID}/contactURLs", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLCONTACTURLS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSchoolContactURL(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLCONTACTURL, 200);
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSchoolContactURL(Role role) throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/schools/schools/{SCHOOLID}/contactURLs", 1l);

    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
    assertOk(role, deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLCONTACTURL, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
  }
}
