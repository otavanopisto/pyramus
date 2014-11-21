package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.MunicipalityPermissions;
import fi.pyramus.rest.model.Municipality;

@RunWith(Parameterized.class)
public class MunicipalityPermissionTestsIT extends AbstractRESTPermissionsTest {

  public MunicipalityPermissionTestsIT(String role) {
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
  
  private MunicipalityPermissions municipalityPermissions = new MunicipalityPermissions();
  
  @Test
  public void testCreateMunicipality() throws NoSuchFieldException {
    Municipality municipality = new Municipality(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");


    assertOk(response, municipalityPermissions, MunicipalityPermissions.CREATE_MUNICIPALITY);
      
//    int id = response.body().jsonPath().getInt("id");
//    
//    given().headers(getAuthHeaders())
//      .delete("/students/municipalities/{ID}?permanent=true", id)
//      .then()
//      .statusCode(204);
  }
  
  @Test
  public void testListMunicipalities() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/municipalities");

    assertOk(response, municipalityPermissions, MunicipalityPermissions.LIST_MUNICIPALITIES);
  }
  
  @Test
  public void testFindMunicipality() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/municipalities/{ID}", 1);

    assertOk(response, municipalityPermissions, MunicipalityPermissions.FIND_MUNICIPALITY);
  }
  
  @Test
  public void testUpdateMunicipality() throws NoSuchFieldException {
    Municipality municipality = new Municipality(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Municipality updateMunicipality = new Municipality(id, "Updated", "UPD", Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateMunicipality)
        .put("/students/municipalities/{ID}", id);
      
      assertOk(response, municipalityPermissions, MunicipalityPermissions.UPDATE_MUNICIPALITY);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/municipalities/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteMunicipality() throws NoSuchFieldException {
    Municipality municipality = new Municipality(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(municipality)
      .post("/students/municipalities");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/municipalities/{ID}", id);

    assertOk(response, municipalityPermissions, MunicipalityPermissions.DELETE_MUNICIPALITY, 204);
  }
}
