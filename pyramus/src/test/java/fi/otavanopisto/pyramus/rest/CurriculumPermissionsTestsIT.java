package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.Curriculum;

@RunWith(Parameterized.class)
public class CurriculumPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CurriculumPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCurriculum() throws NoSuchFieldException {
    Curriculum curriculum = new Curriculum(null, "Test Curriculum", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(curriculum)
      .post("/common/curriculums");

    assertOk(response, commonPermissions, CommonPermissions.CREATE_CURRICULUM, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given()
          .headers(getAdminAuthHeaders())
          .delete("/common/curriculums/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListCurriculums() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/curriculums");
    assertOk(response, commonPermissions, CommonPermissions.LIST_CURRICULUMS, 200);
  }
  
  @Test
  public void testPermissionsFindCurriculum() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/curriculums/{ID}", 1);
    assertOk(response, commonPermissions, CommonPermissions.FIND_CURRICULUM, 200);
  }
  
  @Test
  public void testPermissionsUpdateCurriculum() throws NoSuchFieldException {
    Curriculum curriculum = new Curriculum(null, "Original Curriculum", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(curriculum)
      .post("/common/curriculums");

    Long id = response.body().jsonPath().getLong("id");
    try {
      Curriculum updateCurriculum = new Curriculum(id, "Updated Curriculum", Boolean.FALSE);

      Response updateResponse = given()
          .headers(getAuthHeaders())
          .contentType("application/json")
          .body(updateCurriculum)
          .put("/common/curriculums/{ID}", id);
      
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_CURRICULUM, 200);

    } finally {
      given()
        .headers(getAdminAuthHeaders())
        .delete("/common/curriculums/{ID}?permanent=true", id)
        .then();
    }
  }
  
  @Test
  public void testPermissionsDeleteCurriculum() throws NoSuchFieldException {
    Curriculum curriculum = new Curriculum(null, "Curriculum to be deleted", Boolean.FALSE);
    
    Response response = given()
        .headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(curriculum)
        .post("/common/curriculums");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      Response deleteResponse = given().headers(getAuthHeaders())
        .delete("/common/curriculums/{ID}", id);
      
      assertOk(deleteResponse, commonPermissions, CommonPermissions.ARCHIVE_CURRICULUM, 204);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/curriculums/{ID}?permanent=true", id);
    }
  }

}
