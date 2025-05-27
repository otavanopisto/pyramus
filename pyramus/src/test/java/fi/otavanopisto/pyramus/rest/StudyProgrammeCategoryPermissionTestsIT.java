package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammeCategoryPermissions;
import fi.otavanopisto.pyramus.rest.model.StudyProgrammeCategory;
import io.restassured.response.Response;

public class StudyProgrammeCategoryPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudyProgrammeCategoryPermissions studyProgrammeCategoryPermissions = new StudyProgrammeCategoryPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudyProgrammeCategory(Role role) throws NoSuchFieldException {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "create", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");

    assertOk(role, response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.CREATE_STUDYPROGRAMMECATEGORY);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
  
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void listStudyProgrammeCategories(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studyProgrammeCategories");

    assertOk(role, response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.LIST_STUDYPROGRAMMECATEGORIES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudyProgrammeCategory(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studyProgrammeCategories/{ID}", 1);

    assertOk(role, response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.FIND_STUDYPROGRAMMECATEGORY);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudyProgrammeCategory(Role role) throws NoSuchFieldException {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "Not Updated", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");

    Long id = response.body().jsonPath().getLong("id");
    try {
      StudyProgrammeCategory updateStudyProgrammeCategory = new StudyProgrammeCategory(id, "Updated", 2l, Boolean.FALSE);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudyProgrammeCategory)
        .put("/students/studyProgrammeCategories/{ID}", id);

      assertOk(role, response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.UPDATE_STUDYPROGRAMMECATEGORY);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudyProgrammeCategory(Role role) throws NoSuchFieldException {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "create type", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/studyProgrammeCategories/{ID}", id);
    
    assertOk(role, response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.DELETE_STUDYPROGRAMMECATEGORY, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id);
  }
}
