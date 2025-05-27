package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.EducationType;
import io.restassured.response.Response;

public class EducationTypePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateEducationType(Role role) throws NoSuchFieldException {
    EducationType educationType = new EducationType(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");

    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_EDUCATIONTYPE, 200);
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/educationTypes/{ID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindEducationTypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
        .get("/common/educationTypes");
    
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_EDUCATIONTYPE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindEducationType(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
        .get("/common/educationTypes/{ID}", 1);
    
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_EDUCATIONTYPE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSubject(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
        .get("/common/educationTypes/{ID}/subjects", 2);
    
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_SUBJECTSBYEDUCATIONTYPE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateEducationType(Role role) throws NoSuchFieldException {
    EducationType educationType = new EducationType(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      EducationType updateEducationType = new EducationType(id, "Updated", "UPD", Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateEducationType)
        .put("/common/educationTypes/{ID}", id);
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_EDUCATIONTYPE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/educationTypes/{ID}?permanent=true", id);
    }
  }
//  hmm dur?
//  @ParameterizedTest
//  @EnumSource(Role.class)
//  public void testPermissionsEducationType() {
//    EducationType educationType = new EducationType(null, "create type", "TST", Boolean.FALSE);
//    
//    Response response = given().headers(getAuthHeaders(role))
//      .contentType("application/json")
//      .body(educationType)
//      .post("/common/educationTypes");
//    
//    Long id = new Long(response.body().jsonPath().getInt("id"));
//    assertNotNull(id);
//    
//    given().headers(getAuthHeaders(role)).get("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(200);
//    
//    given().headers(getAuthHeaders(role))
//      .delete("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(204);
//    
//    given().headers(getAuthHeaders(role)).get("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(404);
//    
//    given().headers(getAuthHeaders(role))
//      .delete("/common/educationTypes/{ID}?permanent=true", id)
//      .then()
//      .statusCode(204);
//    
//    given().headers(getAuthHeaders(role)).get("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(404);
//  }
  
}
