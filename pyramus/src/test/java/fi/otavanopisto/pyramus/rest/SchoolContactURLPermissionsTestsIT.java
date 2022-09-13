package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.ContactURL;

@RunWith(Parameterized.class)
public class SchoolContactURLPermissionsTestsIT extends AbstractRESTPermissionsTest {
  
  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public SchoolContactURLPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSchoolContactURL() throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/schools/schools/{ID}/contactURLs", 1l);

    assertOk(response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLCONTACTURL, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
      }
    }
  }
  
  @Test
  public void testPermissionsListSchoolContactURLs() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{ID}/contactURLs", 1l), schoolPermissions, SchoolPermissions.LIST_SCHOOLCONTACTURLS, 200);
  }
  
  @Test
  public void testPermissionsFindSchoolContactURL() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, 1l), schoolPermissions, SchoolPermissions.FIND_SCHOOLCONTACTURL, 200);
  }  

  @Test
  public void testPermissionsDeleteSchoolContactURL() throws NoSuchFieldException {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/schools/schools/{SCHOOLID}/contactURLs", 1l);

    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
    assertOk(deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLCONTACTURL, 204);
    
    if (deleteResponse.statusCode() != 204)
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
  }
}
