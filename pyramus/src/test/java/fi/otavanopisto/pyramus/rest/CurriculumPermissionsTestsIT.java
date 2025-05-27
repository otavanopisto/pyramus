package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.Curriculum;
import io.restassured.response.Response;

public class CurriculumPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCurriculum(Role role) throws NoSuchFieldException {
    Curriculum curriculum = new Curriculum(null, "Test Curriculum", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(curriculum)
      .post("/common/curriculums");

    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_CURRICULUM, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given()
          .headers(getAdminAuthHeaders())
          .delete("/common/curriculums/{ID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCurriculums(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/curriculums");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_CURRICULUMS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCurriculum(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/curriculums/{ID}", 1);
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_CURRICULUM, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCurriculum(Role role) throws NoSuchFieldException {
    Curriculum curriculum = new Curriculum(null, "Original Curriculum", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(curriculum)
      .post("/common/curriculums");

    Long id = response.body().jsonPath().getLong("id");
    try {
      Curriculum updateCurriculum = new Curriculum(id, "Updated Curriculum", Boolean.FALSE);

      Response updateResponse = given()
          .headers(getAuthHeaders(role))
          .contentType("application/json")
          .body(updateCurriculum)
          .put("/common/curriculums/{ID}", id);
      
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_CURRICULUM, 200);

    } finally {
      given()
        .headers(getAdminAuthHeaders())
        .delete("/common/curriculums/{ID}?permanent=true", id)
        .then();
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCurriculum(Role role) throws NoSuchFieldException {
    Curriculum curriculum = new Curriculum(null, "Curriculum to be deleted", Boolean.FALSE);
    
    Response response = given()
        .headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(curriculum)
        .post("/common/curriculums");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      Response deleteResponse = given().headers(getAuthHeaders(role))
        .delete("/common/curriculums/{ID}", id);
      
      assertOk(role, deleteResponse, commonPermissions, CommonPermissions.ARCHIVE_CURRICULUM, 204);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/curriculums/{ID}?permanent=true", id);
    }
  }

}
