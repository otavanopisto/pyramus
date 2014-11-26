package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.controller.permissions.CommonPermissions;
import fi.pyramus.rest.model.Subject;

@RunWith(Parameterized.class)
public class SubjectPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public SubjectPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSubject() throws NoSuchFieldException {
    Subject subject = new Subject(null, "TST", "create subject", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");

    assertOk(response, commonPermissions, CommonPermissions.CREATE_SUBJECT, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id = null;
    if(statusCode.equals(200)){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAuthHeaders())
        .delete("/common/subjects/{ID}?permanent=true", id);
      }
    }

  }
  
  @Test
  public void testPermissionsListSubject() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/subjects");
    assertOk(response, commonPermissions, CommonPermissions.LIST_SUBJECTS, 200);
  }
  
  @Test
  public void testPermissionsFindSubject() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/subjects/{ID}", 1);
    assertOk(response, commonPermissions, CommonPermissions.FIND_SUBJECT, 200);
  }
  
  @Test
  public void testPermissionsUpdateSubject() throws NoSuchFieldException {
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
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Subject updateSubject = new Subject(id, "UPD", "updated", 2l, Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateSubject)
        .put("/common/subjects/{ID}", id);
      
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_SUBJECT, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/subjects/{ID}?permanent=true", id)
        .then();
    }
  }
  
  @Test
  public void testPermissionsDeleteSubject() throws NoSuchFieldException {
    Subject subject = new Subject(null, "DEL", "to be deleted", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAdminAuthHeaders()).get("/common/subjects/{ID}", id)
      .then()
      .statusCode(200);
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/common/subjects/{ID}", id);
    
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_SUBJECT, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/common/subjects/{ID}?permanent=true", id);
  }
//  TODO: testPermissionsListCoursesBySubject ?
}
