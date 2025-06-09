package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.Subject;
import io.restassured.response.Response;

public class SubjectPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSubject(Role role) throws NoSuchFieldException {
    Subject subject = new Subject(null, "TST", "create subject", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");

    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_SUBJECT, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/subjects/{ID}?permanent=true", id);
      }
    }

  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSubject(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/subjects");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_SUBJECTS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSubject(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/subjects/{ID}", 1);
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_SUBJECT, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateSubject(Role role) throws NoSuchFieldException {
    Subject subject = new Subject(null, "NUPD", "not updated", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(subject.getName()))
      .body("code", is(subject.getCode()))
      .body("educationTypeId", is(subject.getEducationTypeId().intValue()))
      .body("archived", is( subject.getArchived() ));
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      Subject updateSubject = new Subject(id, "UPD", "updated", 2l, Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateSubject)
        .put("/common/subjects/{ID}", id);
      
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_SUBJECT, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/subjects/{ID}?permanent=true", id)
        .then();
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSubject(Role role) throws NoSuchFieldException {
    Subject subject = new Subject(null, "DEL", "to be deleted", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");
    
    Long id = response.body().jsonPath().getLong("id");

    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/common/subjects/{ID}", id);
    
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.ARCHIVE_SUBJECT, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/common/subjects/{ID}?permanent=true", id);
  }
//  TODO: testPermissionsListCoursesBySubject ?
}
