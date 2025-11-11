package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.Email;
import io.restassured.response.Response;

public class SchoolEmailPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSchoolEmail(Role role) throws NoSuchFieldException {
    Email email = new Email(null, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(email)
      .post("/schools/schools/{ID}/emails", 1l);
    
    assertOk(role, response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLEMAIL, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSchoolEmails(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{ID}/emails", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLEMAILS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSchoolEmail(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLEMAIL, 200);
  }  

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSchoolEmail(Role role) throws NoSuchFieldException {
    Email email = new Email(null, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/schools/schools/{SCHOOLID}/emails", 1l);
  
    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
    assertOk(role, deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLEMAIL, 204);
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
  }
}
