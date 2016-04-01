package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammePermissions;
import fi.otavanopisto.pyramus.rest.model.StudyProgramme;

@RunWith(Parameterized.class)
public class StudyProgrammePermissionTestsIT extends AbstractRESTPermissionsTest {

  public StudyProgrammePermissionTestsIT(String role) {
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
  
  private StudyProgrammePermissions studyProgrammePermissions = new StudyProgrammePermissions();
  
  @Test
  public void testCreateStudyProgramme() throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, "TST", "create", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    assertOk(response, studyProgrammePermissions, StudyProgrammePermissions.CREATE_STUDYPROGRAMME);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void listStudyProgrammes() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studyProgrammes");
    
    assertOk(response, studyProgrammePermissions, StudyProgrammePermissions.LIST_STUDYPROGRAMMES);
  }
  
  @Test
  public void testFindStudyProgramme() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/studyProgrammes/{ID}", 1);

    assertOk(response, studyProgrammePermissions, StudyProgrammePermissions.FIND_STUDYPROGRAMME);
  }
  
  @Test
  public void testUpdateStudyProgramme() throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, "NOT", "Not Updated", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudyProgramme updateStudyProgramme = new StudyProgramme(id, "UPD", "Updated", 2l, Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudyProgramme)
        .put("/students/studyProgrammes/{ID}", id);

      assertOk(response, studyProgrammePermissions, StudyProgrammePermissions.UPDATE_STUDYPROGRAMME);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammes/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteStudyProgramme() throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, "TST", "create type", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/studyProgrammes/{ID}", id);

    assertOk(response, studyProgrammePermissions, StudyProgrammePermissions.DELETE_STUDYPROGRAMME, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/students/studyProgrammes/{ID}?permanent=true", id);
  }
}
