package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.MunicipalityPermissions;
import fi.otavanopisto.pyramus.rest.model.Municipality;
import io.restassured.response.Response;

public class MunicipalityPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private MunicipalityPermissions municipalityPermissions = new MunicipalityPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateMunicipality(Role role) throws NoSuchFieldException {
    Municipality municipality = new Municipality(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");


    assertOk(role, response, municipalityPermissions, MunicipalityPermissions.CREATE_MUNICIPALITY);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/municipalities/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListMunicipalities(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/municipalities");

    assertOk(role, response, municipalityPermissions, MunicipalityPermissions.LIST_MUNICIPALITIES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindMunicipality(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/municipalities/{ID}", 1);

    assertOk(role, response, municipalityPermissions, MunicipalityPermissions.FIND_MUNICIPALITY);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateMunicipality(Role role) throws NoSuchFieldException {
    Municipality municipality = new Municipality(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");

    Long id = response.body().jsonPath().getLong("id");
    try {
      Municipality updateMunicipality = new Municipality(id, "Updated", "UPD", Boolean.FALSE);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateMunicipality)
        .put("/students/municipalities/{ID}", id);
      
      assertOk(role, response, municipalityPermissions, MunicipalityPermissions.UPDATE_MUNICIPALITY);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/municipalities/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteMunicipality(Role role) throws NoSuchFieldException {
    Municipality municipality = new Municipality(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/municipalities/{ID}", id);

    assertOk(role, response, municipalityPermissions, MunicipalityPermissions.DELETE_MUNICIPALITY, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/municipalities/{ID}?permanent=true", id);
  }
}
