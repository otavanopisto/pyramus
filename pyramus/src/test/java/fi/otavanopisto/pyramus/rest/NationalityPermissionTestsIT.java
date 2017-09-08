package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.NationalityPermissions;
import fi.otavanopisto.pyramus.rest.model.Nationality;

@RunWith(Parameterized.class)
public class NationalityPermissionTestsIT extends AbstractRESTPermissionsTest {

  public NationalityPermissionTestsIT(String role) {
    this.role = role;
  }
  
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  private NationalityPermissions nationalityPermissions = new NationalityPermissions();
  
  @Test
  public void testCreateNationality() throws NoSuchFieldException {
    Nationality nationality = new Nationality(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");

    assertOk(response, nationalityPermissions, NationalityPermissions.CREATE_NATIONALITY);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
    
      given().headers(getAdminAuthHeaders())
        .delete("/students/nationalities/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testListNationalities() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/nationalities");

    assertOk(response, nationalityPermissions, NationalityPermissions.LIST_NATIONALITIES);
  }
  
  @Test
  public void testFindNationality() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/nationalities/{ID}", 1);

    assertOk(response, nationalityPermissions, NationalityPermissions.FIND_NATIONALITY);
  }
  
  @Test
  public void testUpdateNationality() throws NoSuchFieldException {
    Nationality nationality = new Nationality(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Nationality updateNationality = new Nationality(id, "Updated", "UPD", Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateNationality)
        .put("/students/nationalities/{ID}", id);

      assertOk(response, nationalityPermissions, NationalityPermissions.UPDATE_NATIONALITY);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/nationalities/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteNationality() throws NoSuchFieldException {
    Nationality nationality = new Nationality(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/nationalities/{ID}", id);

    assertOk(response, nationalityPermissions, NationalityPermissions.DELETE_NATIONALITY, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/nationalities/{ID}?permanent=true", id);
  }
}
