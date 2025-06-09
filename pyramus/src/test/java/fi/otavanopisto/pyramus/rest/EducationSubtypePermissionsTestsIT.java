package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.EducationSubtype;
import io.restassured.response.Response;

public class EducationSubtypePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateEducationSubtype(Role role) throws NoSuchFieldException {
    EducationSubtype educationSubtype = new EducationSubtype(null, "create sub type", "TST", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());

    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_EDUCATIONSUBTYPE, 200);
         
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        deleteEducationSubtype(educationSubtype.getEducationTypeId(), id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListEducationSubtypes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/educationTypes/{EDUCATIONTYPE}/subtypes", 1);
    
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_EDUCATIONSUBTYPES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindEducationSubtype(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", 1, 1);
    
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_EDUCATIONSUBTYPE, 200);    
  }
    
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateEducationSubtype(Role role) throws NoSuchFieldException {
    Long updateEducationTypeId = 1l;
    EducationSubtype educationSubtype = new EducationSubtype(null, "Not Updated", "NOT", 2l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());

    Long id = response.body().jsonPath().getLong("id");

    EducationSubtype updateSubtype = new EducationSubtype(id, "Updated", "UPD", updateEducationTypeId, Boolean.FALSE);

    Response updateResponse = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(updateSubtype)
      .put("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), updateSubtype.getId());
    
    assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_EDUCATIONSUBTYPE, 200);

    if (updateResponse.getStatusCode() == 200) {
      deleteEducationSubtype(updateEducationTypeId, id);
    } else {
      deleteEducationSubtype(educationSubtype.getEducationTypeId(), id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteEducationSubtype(Role role) throws NoSuchFieldException {
    EducationSubtype educationSubtype = new EducationSubtype(null, "create subtype", "TST", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());
    
    Long id = response.body().jsonPath().getLong("id");
        
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), id);
    
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.DELETE_EDUCATIONSUBTYPE, 204);
    deleteEducationSubtype(educationSubtype.getEducationTypeId(), id);
  }

  private void deleteEducationSubtype(Long educationTypeId, Long id) {
    given().headers(getAdminAuthHeaders())
      .delete("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}?permanent=true", educationTypeId, id)
      .then()
      .statusCode(204);
  }
}
