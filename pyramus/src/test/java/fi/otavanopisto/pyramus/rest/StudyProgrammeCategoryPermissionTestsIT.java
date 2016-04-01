package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammeCategoryPermissions;
import fi.otavanopisto.pyramus.rest.model.StudyProgrammeCategory;

@RunWith(Parameterized.class)
public class StudyProgrammeCategoryPermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudyProgrammeCategoryPermissionTestsIT(String role) {
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

  private StudyProgrammeCategoryPermissions studyProgrammeCategoryPermissions = new StudyProgrammeCategoryPermissions();
  
  @Test
  public void testCreateStudyProgrammeCategory() throws NoSuchFieldException {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "create", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");

    assertOk(response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.CREATE_STUDYPROGRAMMECATEGORY);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
  
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void listStudyProgrammeCategories() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studyProgrammeCategories");

    assertOk(response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.LIST_STUDYPROGRAMMECATEGORIES);
  }
  
  @Test
  public void testFindStudyProgrammeCategory() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studyProgrammeCategories/{ID}", 1);

    assertOk(response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.FIND_STUDYPROGRAMMECATEGORY);
  }
  
  @Test
  public void testUpdateStudyProgrammeCategory() throws NoSuchFieldException {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "Not Updated", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudyProgrammeCategory updateStudyProgrammeCategory = new StudyProgrammeCategory(id, "Updated", 2l, Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudyProgrammeCategory)
        .put("/students/studyProgrammeCategories/{ID}", id);

      assertOk(response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.UPDATE_STUDYPROGRAMMECATEGORY);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteStudyProgrammeCategory() throws NoSuchFieldException {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "create type", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/studyProgrammeCategories/{ID}", id);
    
    assertOk(response, studyProgrammeCategoryPermissions, StudyProgrammeCategoryPermissions.DELETE_STUDYPROGRAMMECATEGORY, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id);
  }
}
