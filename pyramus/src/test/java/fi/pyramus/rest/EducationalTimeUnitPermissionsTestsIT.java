package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.CommonPermissions;
import fi.pyramus.rest.model.EducationalTimeUnit;

@RunWith(Parameterized.class)
public class EducationalTimeUnitPermissionsTestsIT extends AbstractRESTPermissionsTest {
  
private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public EducationalTimeUnitPermissionsTestsIT(String role){
    this.role = role;
  }

  @Test
  public void testPermissionsCreateEducationalTimeUnit() throws NoSuchFieldException {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "create unit", "", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");
    
    assertOk(response, commonPermissions, CommonPermissions.CREATE_EDUCATIONALTIMEUNIT, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
        .delete("/common/educationalTimeUnits/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListEducationalTimeUnits() throws NoSuchFieldException {
    Response response =  given().headers(getAuthHeaders())
      .get("/common/educationalTimeUnits");
    
    assertOk(response, commonPermissions, CommonPermissions.LIST_EDUCATIONALTIMEUNITS, 200);
  }
  
  @Test
  public void testPermissionsFindEducationalTimeUnit() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/educationalTimeUnits/{ID}", 1);
    
    assertOk(response, commonPermissions, CommonPermissions.FIND_EDUCATIONALTIMEUNIT, 200);
  }
  
  @Test
  public void testPermissionsUpdateEducationalTimeUnit() throws NoSuchFieldException {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      EducationalTimeUnit updateTimeUnit = new EducationalTimeUnit(id, "updated unit", "sym", 2d, Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateTimeUnit)
        .put("/common/educationalTimeUnits/{ID}", id);
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_EDUCATIONALTIMEUNIT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/educationalTimeUnits/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteEducationalTimeUnit() throws NoSuchFieldException {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");
 
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/common/educationalTimeUnits/{ID}", id);
    assertOk(deleteResponse, commonPermissions, CommonPermissions.ARCHIVE_EDUCATIONALTIMEUNIT, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/common/educationalTimeUnits/{ID}?permanent=true", id);
  }
}
