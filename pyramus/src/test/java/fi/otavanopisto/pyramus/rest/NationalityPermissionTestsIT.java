package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.NationalityPermissions;
import fi.otavanopisto.pyramus.rest.model.Nationality;
import io.restassured.response.Response;

public class NationalityPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private NationalityPermissions nationalityPermissions = new NationalityPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateNationality(Role role) throws NoSuchFieldException {
    Nationality nationality = new Nationality(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");

    assertOk(role, response, nationalityPermissions, NationalityPermissions.CREATE_NATIONALITY);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
    
      given().headers(getAdminAuthHeaders())
        .delete("/students/nationalities/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListNationalities(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/nationalities");

    assertOk(role, response, nationalityPermissions, NationalityPermissions.LIST_NATIONALITIES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindNationality(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/nationalities/{ID}", 1);

    assertOk(role, response, nationalityPermissions, NationalityPermissions.FIND_NATIONALITY);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateNationality(Role role) throws NoSuchFieldException {
    Nationality nationality = new Nationality(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");

    Long id = response.body().jsonPath().getLong("id");
    try {
      Nationality updateNationality = new Nationality(id, "Updated", "UPD", Boolean.FALSE);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateNationality)
        .put("/students/nationalities/{ID}", id);

      assertOk(role, response, nationalityPermissions, NationalityPermissions.UPDATE_NATIONALITY);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/nationalities/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteNationality(Role role) throws NoSuchFieldException {
    Nationality nationality = new Nationality(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(nationality)
      .post("/students/nationalities");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/nationalities/{ID}", id);

    assertOk(role, response, nationalityPermissions, NationalityPermissions.DELETE_NATIONALITY, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/nationalities/{ID}?permanent=true", id);
  }
}
