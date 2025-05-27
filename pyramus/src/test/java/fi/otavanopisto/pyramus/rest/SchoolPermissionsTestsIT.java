package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.School;
import io.restassured.response.Response;

public class SchoolPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateSchool(Role role) throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    
    School school = new School(null, "TST", "created", Arrays.asList("tag1", "tag2"), 1l, "additional info", Boolean.FALSE, variables);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
    
    assertOk(role, response, schoolPermissions, SchoolPermissions.CREATE_SCHOOL, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/schools/schools/{ID}?permanent=true", id);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListSchools(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools"), schoolPermissions, SchoolPermissions.LIST_SCHOOLS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindSchool(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/schools/schools/{ID}", 1), schoolPermissions, SchoolPermissions.FIND_SCHOOL, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateSchool(Role role) throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    
    School school = new School(null, "TST", "notupdated", Arrays.asList("tag1", "tag2"), 1l, "not updated info", Boolean.FALSE, variables);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
      
    Long id = response.body().jsonPath().getLong("id");
    try {
      Map<String, String> updateVariables = new HashMap<>();

      School updateSchool = new School(id, "UPD", "updated", Arrays.asList("tag2", "tag3"), 2l, "updated info", Boolean.FALSE, updateVariables);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateSchool)
        .put("/schools/schools/{ID}", id);
      assertOk(role, updateResponse, schoolPermissions, SchoolPermissions.UPDATE_SCHOOL, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteSchool(Role role) throws NoSuchFieldException {
    School school = new School(null, "TST", "to be deleted", Arrays.asList("tag1", "tag2"), 1l, "additional", Boolean.FALSE, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
      
    Long id = response.body().jsonPath().getLong("id");
    
    assertOk(role, given().headers(getAuthHeaders(role))
      .delete("/schools/schools/{ID}", id), schoolPermissions, SchoolPermissions.DELETE_SCHOOL, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/schools/schools/{ID}?permanent=true", id);
  }
}
