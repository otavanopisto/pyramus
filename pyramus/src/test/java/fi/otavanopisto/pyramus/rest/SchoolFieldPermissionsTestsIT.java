package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.SchoolField;
import io.restassured.response.Response;

public class SchoolFieldPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSchoolField(Role role) throws NoSuchFieldException {
    SchoolField schoolField = new SchoolField(null, "to be created", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");
    
    assertOk(role, response, schoolPermissions, SchoolPermissions.CREATE_SCHOOLFIELD, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schoolFields/{ID}?permanent=true", id);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSchoolFields(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schoolFields"), schoolPermissions, SchoolPermissions.LIST_SCHOOLFIELDS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSchoolField(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
        .get("/schools/schoolFields/{ID}", 1), schoolPermissions, SchoolPermissions.FIND_SCHOOLFIELD, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateSchoolField(Role role) throws NoSuchFieldException {
    SchoolField schoolField = new SchoolField(null, "not updated", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");

    Long id = response.body().jsonPath().getLong("id");
    try {
      SchoolField updateSchoolField = new SchoolField(id, "updated", Boolean.FALSE);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateSchoolField)
        .put("/schools/schoolFields/{ID}", id);
      assertOk(role, updateResponse, schoolPermissions, SchoolPermissions.UPDATE_SCHOOLFIELD, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schoolFields/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSchoolField(Role role) throws NoSuchFieldException {
    SchoolField schoolField = new SchoolField(null, "to be deleted", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");

    Long id = response.body().jsonPath().getLong("id");

    assertOk(role, given().headers(getAuthHeaders(role))
        .delete("/schools/schoolFields/{ID}", id), schoolPermissions, SchoolPermissions.DELETE_SCHOOLFIELD, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/schools/schoolFields/{ID}?permanent=true", id);
  }
}
