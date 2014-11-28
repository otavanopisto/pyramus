package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.CommonPermissions;
import fi.pyramus.rest.model.EducationType;

@RunWith(Parameterized.class)
public class EducationTypePermissionsTestsIT extends AbstractRESTPermissionsTest {
  
  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public EducationTypePermissionsTestsIT(String role) {
    this.role = role;
  }

  @Test
  public void testPermissionsCreateEducationType() throws NoSuchFieldException {
    EducationType educationType = new EducationType(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");

    assertOk(response, commonPermissions, CommonPermissions.CREATE_EDUCATIONTYPE, 200);
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/educationTypes/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsFindEducationTypes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
        .get("/common/educationTypes");
    
    assertOk(response, commonPermissions, CommonPermissions.FIND_EDUCATIONTYPE, 200);
  }
  
  @Test
  public void testPermissionsFindEducationType() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
        .get("/common/educationTypes/{ID}", 1);
    
    assertOk(response, commonPermissions, CommonPermissions.FIND_EDUCATIONTYPE, 200);
  }
  
  @Test
  public void testPermissionsListSubject() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
        .get("/common/educationTypes/{ID}/subjects", 2);
    
    assertOk(response, commonPermissions, CommonPermissions.LIST_SUBJECTS, 200);
  }
  
  @Test
  public void testPermissionsUpdateEducationType() throws NoSuchFieldException {
    EducationType educationType = new EducationType(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      EducationType updateEducationType = new EducationType(id, "Updated", "UPD", Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEducationType)
        .put("/common/educationTypes/{ID}", id);
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_EDUCATIONTYPE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/educationTypes/{ID}?permanent=true", id);
    }
  }
//  hmm dur?
//  @Test
//  public void testPermissionsEducationType() {
//    EducationType educationType = new EducationType(null, "create type", "TST", Boolean.FALSE);
//    
//    Response response = given().headers(getAuthHeaders())
//      .contentType("application/json")
//      .body(educationType)
//      .post("/common/educationTypes");
//    
//    Long id = new Long(response.body().jsonPath().getInt("id"));
//    assertNotNull(id);
//    
//    given().headers(getAuthHeaders()).get("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(200);
//    
//    given().headers(getAuthHeaders())
//      .delete("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(204);
//    
//    given().headers(getAuthHeaders()).get("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(404);
//    
//    given().headers(getAuthHeaders())
//      .delete("/common/educationTypes/{ID}?permanent=true", id)
//      .then()
//      .statusCode(204);
//    
//    given().headers(getAuthHeaders()).get("/common/educationTypes/{ID}", id)
//      .then()
//      .statusCode(404);
//  }
  
}
