package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.Email;

@RunWith(Parameterized.class)
public class SchoolEmailPermissionsTestsIT extends AbstractRESTPermissionsTest{
  
  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public SchoolEmailPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSchoolEmail() throws NoSuchFieldException {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/schools/schools/{ID}/emails", 1l);
    
    assertOk(response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLEMAIL, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
      }
    }
  }
  
  @Test
  public void testPermissionsListSchoolEmails() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{ID}/emails", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLEMAILS, 200);
  }
  
  @Test
  public void testPermissionsFindSchoolEmail() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLEMAIL, 200);
  }  

  @Test
  public void testPermissionsDeleteSchoolEmail() throws NoSuchFieldException {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/schools/schools/{SCHOOLID}/emails", 1l);
  
    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
    assertOk(deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLEMAIL, 204);
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
  }
}
