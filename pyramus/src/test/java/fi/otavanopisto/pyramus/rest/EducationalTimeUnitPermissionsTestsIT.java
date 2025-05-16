package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit;
import io.restassured.response.Response;

public class EducationalTimeUnitPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateEducationalTimeUnit(Role role) throws NoSuchFieldException {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "create unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");
    
    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_EDUCATIONALTIMEUNIT, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/common/educationalTimeUnits/{ID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListEducationalTimeUnits(Role role) throws NoSuchFieldException {
    Response response =  given().headers(getAuthHeaders(role))
      .get("/common/educationalTimeUnits");
    
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_EDUCATIONALTIMEUNITS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindEducationalTimeUnit(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/educationalTimeUnits/{ID}", 1);
    
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_EDUCATIONALTIMEUNIT, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateEducationalTimeUnit(Role role) throws NoSuchFieldException {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");

    Long id = response.body().jsonPath().getLong("id");
    try {
      EducationalTimeUnit upOffsetDateTimeUnit = new EducationalTimeUnit(id, "updated unit", "sym", 2d, Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(upOffsetDateTimeUnit)
        .put("/common/educationalTimeUnits/{ID}", id);
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_EDUCATIONALTIMEUNIT, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/educationalTimeUnits/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteEducationalTimeUnit(Role role) throws NoSuchFieldException {
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit(null, "not updated unit", "sym", 1d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationalTimeUnit)
      .post("/common/educationalTimeUnits");
 
    Long id = response.body().jsonPath().getLong("id");
    
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/common/educationalTimeUnits/{ID}", id);
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.ARCHIVE_EDUCATIONALTIMEUNIT, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/common/educationalTimeUnits/{ID}?permanent=true", id);
  }
}
