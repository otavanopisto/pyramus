package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.rest.model.School;

@RunWith(Parameterized.class)
public class SchoolPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private SchoolPermissions schoolPermissions = new SchoolPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public SchoolPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateSchool() throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    
    School school = new School(null, "TST", "created", Arrays.asList("tag1", "tag2"), 1l, "additional info", Boolean.FALSE, variables);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
    
    assertOk(response, schoolPermissions, SchoolPermissions.CREATE_SCHOOL, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{ID}?permanent=true", id);
      }
    }
  }

  @Test
  public void testPermissionsListSchools() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools"), schoolPermissions, SchoolPermissions.LIST_SCHOOLS, 200);
  }
  
  @Test
  public void testPermissionsFindSchool() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/schools/schools/{ID}", 1), schoolPermissions, SchoolPermissions.FIND_SCHOOL, 200);
  }
  
  @Test
  public void testPermissionsUpdateSchool() throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    
    School school = new School(null, "TST", "notupdated", Arrays.asList("tag1", "tag2"), 1l, "not updated info", Boolean.FALSE, variables);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Map<String, String> updateVariables = new HashMap<>();

      School updateSchool = new School(id, "UPD", "updated", Arrays.asList("tag2", "tag3"), 2l, "updated info", Boolean.FALSE, updateVariables);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateSchool)
        .put("/schools/schools/{ID}", id);
      assertOk(updateResponse, schoolPermissions, SchoolPermissions.UPDATE_SCHOOL, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/schools/schools/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteSchool() throws NoSuchFieldException {
    School school = new School(null, "TST", "to be deleted", Arrays.asList("tag1", "tag2"), 1l, "additional", Boolean.FALSE, null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    assertOk(given().headers(getAuthHeaders())
      .delete("/schools/schools/{ID}", id), schoolPermissions, SchoolPermissions.DELETE_SCHOOL, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/schools/schools/{ID}?permanent=true", id);
  }
}
