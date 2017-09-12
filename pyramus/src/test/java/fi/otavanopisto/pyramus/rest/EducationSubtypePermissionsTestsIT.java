package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.EducationSubtype;

@RunWith(Parameterized.class)
public class EducationSubtypePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public EducationSubtypePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateEducationSubtype() throws NoSuchFieldException {
    EducationSubtype educationSubtype = new EducationSubtype(null, "create sub type", "TST", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());

    assertOk(response, commonPermissions, CommonPermissions.CREATE_EDUCATIONSUBTYPE, 200);
         
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        deleteEducationSubtype(educationSubtype.getEducationTypeId(), id);
      }
    }
  }
  
  @Test
  public void testPermissionsListEducationSubtypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/educationTypes/{EDUCATIONTYPE}/subtypes", 1);
    
    assertOk(response, commonPermissions, CommonPermissions.LIST_EDUCATIONSUBTYPES, 200);
  }
  
  @Test
  public void testPermissionsFindEducationSubtype() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", 1, 1);
    
    assertOk(response, commonPermissions, CommonPermissions.FIND_EDUCATIONSUBTYPE, 200);    
  }
    
  @Test
  public void testPermissionsUpdateEducationSubtype() throws NoSuchFieldException {
    Long updateEducationTypeId = 1l;
    EducationSubtype educationSubtype = new EducationSubtype(null, "Not Updated", "NOT", 2l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());

    Long id = new Long(response.body().jsonPath().getInt("id"));

    EducationSubtype updateSubtype = new EducationSubtype(id, "Updated", "UPD", updateEducationTypeId, Boolean.FALSE);

    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateSubtype)
      .put("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), updateSubtype.getId());
    
    assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_EDUCATIONSUBTYPE, 200);

    if (updateResponse.getStatusCode() == 200) {
      deleteEducationSubtype(updateEducationTypeId, id);
    } else {
      deleteEducationSubtype(educationSubtype.getEducationTypeId(), id);
    }
  }
  
  @Test
  public void testPermissionsDeleteEducationSubtype() throws NoSuchFieldException {
    EducationSubtype educationSubtype = new EducationSubtype(null, "create subtype", "TST", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
        
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), id);
    
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_EDUCATIONSUBTYPE, 204);
    deleteEducationSubtype(educationSubtype.getEducationTypeId(), id);
  }

  private void deleteEducationSubtype(Long educationTypeId, Long id) {
    given().headers(getAdminAuthHeaders())
      .delete("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}?permanent=true", educationTypeId, id)
      .then()
      .statusCode(204);
  }
}
