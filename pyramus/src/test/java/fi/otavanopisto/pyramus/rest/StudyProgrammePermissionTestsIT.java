package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.EnumSet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammePermissions;
import fi.otavanopisto.pyramus.rest.model.Organization;
import fi.otavanopisto.pyramus.rest.model.StudyProgramme;
import io.restassured.response.Response;

public class StudyProgrammePermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private static Long TEST_ORGANIZATION_ID = 1L;
  private static Long TEST_CATEGORY_ID = 1L;

  private StudyProgrammePermissions studyProgrammePermissions = new StudyProgrammePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudyProgramme(Role role) throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, TEST_ORGANIZATION_ID, "TST", "create", TEST_CATEGORY_ID, null, Boolean.FALSE, Boolean.FALSE, null);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    assertOk(role, response, studyProgrammePermissions, StudyProgrammePermissions.CREATE_STUDYPROGRAMME);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void listStudyProgrammes(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studyProgrammes");
    
    assertOk(role, response, studyProgrammePermissions, StudyProgrammePermissions.LIST_STUDYPROGRAMMES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudyProgramme(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/studyProgrammes/{ID}", 1);

    assertOk(role, response, studyProgrammePermissions, StudyProgrammePermissions.FIND_STUDYPROGRAMME);
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCantFindStudyProgrammeFromAnotherOrganization(Role role) throws NoSuchFieldException {
    // Administrator and trusted system have access to different organization studyprogrammes
    if (!EnumSet.of(Role.ADMINISTRATOR, Role.TRUSTED_SYSTEM).contains(role)) {
      Organization organization = tools().createOrganization("testCantFindStudyProgrammeFromAnotherOrganization");
      try {
        StudyProgramme studyProgramme = tools().createStudyProgramme(organization.getId(), "TSTX", "testCantFindStudyProgrammeFromAnotherOrganization", TEST_CATEGORY_ID);
        try {
          Response response = given().headers(getAuthHeaders(role))
            .get("/students/studyProgrammes/{ID}", studyProgramme.getId());

          assertEquals(String.format("Role %s can access studyprogramme it shouldn't be able to.", role), 403, response.statusCode());
        } finally {
          tools().deleteStudyProgramme(studyProgramme);
        }
      } finally {
        tools().deleteOrganization(organization);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudyProgramme(Role role) throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, TEST_ORGANIZATION_ID, "NOT", "Not Updated", TEST_CATEGORY_ID, null, Boolean.FALSE, Boolean.FALSE, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    Long id = response.body().jsonPath().getLong("id");
    try {
      StudyProgramme updateStudyProgramme = new StudyProgramme(id, TEST_ORGANIZATION_ID, "UPD", "Updated", 2l, null, Boolean.FALSE, Boolean.FALSE, null);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudyProgramme)
        .put("/students/studyProgrammes/{ID}", id);

      assertOk(role, response, studyProgrammePermissions, StudyProgrammePermissions.UPDATE_STUDYPROGRAMME);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/studyProgrammes/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudyProgramme(Role role) throws NoSuchFieldException {
    StudyProgramme studyProgramme = new StudyProgramme(null, TEST_ORGANIZATION_ID, "TST", "create type", TEST_CATEGORY_ID, null, Boolean.FALSE, Boolean.FALSE, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/studyProgrammes/{ID}", id);

    assertOk(role, response, studyProgrammePermissions, StudyProgrammePermissions.DELETE_STUDYPROGRAMME, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/students/studyProgrammes/{ID}?permanent=true", id);
  }
}
