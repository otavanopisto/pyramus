package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammePermissions;
import fi.otavanopisto.pyramus.rest.model.Organization;
import fi.otavanopisto.pyramus.rest.model.StudyProgramme;
import io.restassured.response.Response;

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
  
  private static Long TEST_ORGANIZATION_ID = 1L;
  private static Long TEST_CATEGORY_ID = 1L;

  private StudyProgrammePermissions studyProgrammePermissions = new StudyProgrammePermissions();
  
  @Test
  public void testCreateStudyProgramme() throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, TEST_ORGANIZATION_ID, "TST", "create", TEST_CATEGORY_ID, null, Boolean.FALSE, Boolean.FALSE);
    
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
  public void testCantFindStudyProgrammeFromAnotherOrganization() throws NoSuchFieldException {
    // Administrator and trusted system have access to different organization studyprogrammes
    if (!isCurrentRole(Role.ADMINISTRATOR, Role.TRUSTED_SYSTEM)) {
      Organization organization = tools().createOrganization("testCantFindStudyProgrammeFromAnotherOrganization");
      try {
        StudyProgramme studyProgramme = tools().createStudyProgramme(organization.getId(), "TSTX", "testCantFindStudyProgrammeFromAnotherOrganization", TEST_CATEGORY_ID);
        try {
          Response response = given().headers(getAuthHeaders())
            .get("/students/studyProgrammes/{ID}", studyProgramme.getId());

          assertEquals(String.format("Role %s can access studyprogramme it shouldn't be able to.", getRole()), 403, response.statusCode());
        } finally {
          tools().deleteStudyProgramme(studyProgramme);
        }
      } finally {
        tools().deleteOrganization(organization);
      }
    }
  }

  @Test
  public void testUpdateStudyProgramme() throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, TEST_ORGANIZATION_ID, "NOT", "Not Updated", TEST_CATEGORY_ID, null, Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    Long id = response.body().jsonPath().getLong("id");
    try {
      StudyProgramme updateStudyProgramme = new StudyProgramme(id, TEST_ORGANIZATION_ID, "UPD", "Updated", 2l, null, Boolean.FALSE, Boolean.FALSE);

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
    StudyProgramme studyProgramme = new StudyProgramme(null, TEST_ORGANIZATION_ID, "TST", "create type", TEST_CATEGORY_ID, null, Boolean.FALSE, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders())
      .delete("/students/studyProgrammes/{ID}", id);

    assertOk(response, studyProgrammePermissions, StudyProgrammePermissions.DELETE_STUDYPROGRAMME, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/students/studyProgrammes/{ID}?permanent=true", id);
  }
}
