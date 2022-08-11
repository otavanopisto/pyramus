package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.OffsetDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.ModulePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseLength;
import fi.otavanopisto.pyramus.rest.model.CourseModule;
import fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit;
import fi.otavanopisto.pyramus.rest.model.Module;
import fi.otavanopisto.pyramus.rest.model.Subject;

@RunWith(Parameterized.class)
public class ModulePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private ModulePermissions modulePermissions = new ModulePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public ModulePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testCreateModule() throws NoSuchFieldException {
    Set<CourseModule> courseModules = new HashSet<>(Arrays.asList(createCourseModule(1l, 111, 1d, 1l)));
    Module module = new Module(null,
        "Create test", 
        null, 
        null, 
        "Course for testing course creation", 
        Boolean.FALSE, 
        222l,
        null,
        null,
        null,
        null,
        courseModules);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules/");
    assertOk(response, modulePermissions, ModulePermissions.CREATE_MODULE, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
        .delete("/modules/modules/{ID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListModules() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/modules/modules");
    assertOk(response, modulePermissions, ModulePermissions.LIST_MODULES, 200);
  }
  
  @Test
  public void testPermissionsFindModule() throws NoSuchFieldException {
     Response response = given().headers(getAuthHeaders())
      .get("/modules/modules/{ID}", 1);
     assertOk(response, modulePermissions, ModulePermissions.FIND_MODULE, 200);
  }
  
  @Test
  public void testPermissionsUpdateModule() throws NoSuchFieldException {
    Set<CourseModule> courseModules = new HashSet<>(Arrays.asList(createCourseModule(1l, 111, 1d, 1l)));
    Module module = new Module(null,
        "not updated", 
        null, 
        null, 
        "not updated module", 
        Boolean.FALSE, 
        222l,
        null,
        null,
        null,
        null,
        courseModules);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    Long courseModuleId = new Long(response.body().jsonPath().getInt("courseModules[0].id"));
   
    try {
      courseModules = new HashSet<>(Arrays.asList(createCourseModule(courseModuleId, 2l, 222, 2d, 2l)));
      Module updateModule = new Module(id,
          "updated", 
          null, 
          null, 
          "updated module", 
          Boolean.FALSE, 
          333l,
          null,
          null,
          null,
          null,
          courseModules);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateModule)
        .put("/modules/modules/{ID}", id);
      assertOk(updateResponse, modulePermissions, ModulePermissions.UPDATE_MODULE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/modules/modules/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteModule() throws NoSuchFieldException {
    Set<CourseModule> courseModules = new HashSet<>(Arrays.asList(createCourseModule(1l, 111, 1d, 1l)));
    Module module = new Module(null,
        "not updated", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "not updated module", 
        Boolean.FALSE, 
        222l,
        1l,
        1l,
        null,
        null,
        courseModules);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules");
    Long id = new Long(response.body().jsonPath().getInt("id"));
   
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/modules/modules/{ID}", id);
    assertOk(deleteResponse, modulePermissions, ModulePermissions.DELETE_MODULE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/modules/modules/{ID}?permanent=true", id);
  }
  
  @Test
  public void testPermissionsListCoursesByModule() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/modules/modules/{MODULEID}/courses", 1l);
    assertOk(response, modulePermissions, ModulePermissions.LIST_COURSESBYMODULE, 200);
  }
  
  @Test
  public void testPermissionsListProjectsByModule() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/modules/modules/{MODULEID}/projects", 1l);
    assertOk(response, modulePermissions, ModulePermissions.LIST_PROJECTSBYMODULE, 200);
  }

  private CourseModule createCourseModule(long subjectId, int courseNumber, double courseLengthUnits, long courseLengthUnitId) {
    return createCourseModule(null, subjectId, courseNumber, courseLengthUnits, courseLengthUnitId);
  }

  private CourseModule createCourseModule(Long courseModuleId, long subjectId, int courseNumber, double courseLengthUnits, long courseLengthUnitId) {
    EducationalTimeUnit courseLengthUnit = new EducationalTimeUnit(courseLengthUnitId, null, null, null, false);
    CourseLength courseLength = new CourseLength(null, null, courseLengthUnits, courseLengthUnit);
    Subject subject = new Subject(subjectId, null, null, null, false);
    return new CourseModule(courseModuleId, subject, courseNumber, courseLength);
  }

}
