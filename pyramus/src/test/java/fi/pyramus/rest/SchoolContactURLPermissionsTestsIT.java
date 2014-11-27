package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.pyramus.rest.model.ContactURL;

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
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.equals(200)){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAuthHeaders())
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

    Long id = new Long(response.body().jsonPath().getInt("id"));

    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
    assertOk(deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLCONTACTURL, 204);
    
    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.equals(204))
      given().headers(getAdminAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/contactURLs/{ID}", 1l, id);
  }
}
