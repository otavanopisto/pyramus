package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.SchoolField;

@RunWith(Parameterized.class)
public class SchoolFieldPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public SchoolFieldPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSchoolField() throws NoSuchFieldException {
    SchoolField schoolField = new SchoolField(null, "to be created", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");
    
    assertOk(response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLFIELD, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schoolFields/{ID}?permanent=true", id);
      }
    }
  }

  @Test
  public void testPermissionsListSchoolFields() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schoolFields"), schoolPermissions, SchoolPermissions.LIST_SCHOOLFIELDS, 200);
  }
  
  @Test
  public void testPermissionsFindSchoolField() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
        .get("/schools/schoolFields/{ID}", 1), schoolPermissions, SchoolPermissions.FIND_SCHOOLFIELD, 200);
  }
  
  @Test
  public void testPermissionsUpdateSchoolField() throws NoSuchFieldException {
    SchoolField schoolField = new SchoolField(null, "not updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");

    Long id = response.body().jsonPath().getLong("id");
    try {
      SchoolField updateSchoolField = new SchoolField(id, "updated", Boolean.FALSE);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateSchoolField)
        .put("/schools/schoolFields/{ID}", id);
      assertOk(updateResponse, schoolPermissions, SchoolPermissions.UPDATE_SCHOOLFIELD, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schoolFields/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteSchoolField() throws NoSuchFieldException {
    SchoolField schoolField = new SchoolField(null, "to be deleted", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");

    Long id = response.body().jsonPath().getLong("id");

    assertOk(given().headers(getAuthHeaders())
        .delete("/schools/schoolFields/{ID}", id), schoolPermissions, SchoolPermissions.DELETE_SCHOOLFIELD, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/schools/schoolFields/{ID}?permanent=true", id);
  }
}
