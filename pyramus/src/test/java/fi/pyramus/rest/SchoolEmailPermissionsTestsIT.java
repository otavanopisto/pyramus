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
import fi.pyramus.rest.model.Email;

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
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
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
  
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
    assertOk(deleteResponse, schoolPermissions, SchoolPermissions.DELETE_SCHOOLEMAIL, 204);
    Long statusCode = new Long(deleteResponse.statusCode());
    if(!statusCode.toString().equals("204"))
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{SCHOOLID}/emails/{ID}", 1l, id);
  }
}
