package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.ModulePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseLength;
import fi.otavanopisto.pyramus.rest.model.CourseModule;
import fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit;
import fi.otavanopisto.pyramus.rest.model.Module;
import fi.otavanopisto.pyramus.rest.model.Subject;
import io.restassured.response.Response;

public class ModulePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private ModulePermissions modulePermissions = new ModulePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateModule(Role role) throws NoSuchFieldException {
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

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(module)
      .post("/modules/modules/");
    assertOk(role, response, modulePermissions, ModulePermissions.CREATE_MODULE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/modules/modules/{ID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListModules(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/modules/modules");
    assertOk(role, response, modulePermissions, ModulePermissions.LIST_MODULES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindModule(Role role) throws NoSuchFieldException {
     Response response = given().headers(getAuthHeaders(role))
      .get("/modules/modules/{ID}", 1);
     assertOk(role, response, modulePermissions, ModulePermissions.FIND_MODULE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateModule(Role role) throws NoSuchFieldException {
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

    Long id = response.body().jsonPath().getLong("id");
    Long courseModuleId = response.body().jsonPath().getLong("courseModules[0].id");
   
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
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateModule)
        .put("/modules/modules/{ID}", id);
      assertOk(role, updateResponse, modulePermissions, ModulePermissions.UPDATE_MODULE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/modules/modules/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteModule(Role role) throws NoSuchFieldException {
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
    Long id = response.body().jsonPath().getLong("id");
   
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/modules/modules/{ID}", id);
    assertOk(role, deleteResponse, modulePermissions, ModulePermissions.DELETE_MODULE, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/modules/modules/{ID}?permanent=true", id);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCoursesByModule(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/modules/modules/{MODULEID}/courses", 1l);
    assertOk(role, response, modulePermissions, ModulePermissions.LIST_COURSESBYMODULE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListProjectsByModule(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/modules/modules/{MODULEID}/projects", 1l);
    assertOk(role, response, modulePermissions, ModulePermissions.LIST_PROJECTSBYMODULE, 200);
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
