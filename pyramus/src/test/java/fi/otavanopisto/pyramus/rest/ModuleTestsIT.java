package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import io.restassured.response.Response;
import fi.otavanopisto.pyramus.rest.model.CourseLength;
import fi.otavanopisto.pyramus.rest.model.CourseModule;
import fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit;
import fi.otavanopisto.pyramus.rest.model.Module;
import fi.otavanopisto.pyramus.rest.model.Subject;

public class ModuleTestsIT extends AbstractRESTServiceTest {

  /**
   * TODO coursemodule tests for module and course
   */
  
  @Test
  public void testCreateModule() {
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
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(module.getName()))
      .body("description", is( module.getDescription() ))
      .body("creatorId", not(is((Long) null )))
      .body("lastModifierId", not(is((Long) null )))
      .body("created", not(is((String) null )))
      .body("lastModified", not(is((String) null )))
      .body("courseModules.size()", is(1))
      .body("courseModules[0].subject.id", is(firstCourseModule(module).getSubject().getId().intValue()))
      .body("courseModules[0].courseNumber", is(firstCourseModule(module).getCourseNumber()))
      .body("courseModules[0].courseLength.units", is(firstCourseModule(module).getCourseLength().getUnits().floatValue()))
      .body("courseModules[0].courseLength.unit.id", is(firstCourseModule(module).getCourseLength().getUnit().getId().intValue()))
      .body("maxParticipantCount", is( module.getMaxParticipantCount().intValue() ))
      .body("archived", is( module.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/modules/modules/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListModules() {
    given().headers(getAuthHeaders())
      .get("/modules/modules")
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("name[0]", is("Test Module #1" ))
      .body("description[0]", is("Module #1 for testing"))
      .body("archived[0]", is( false ));
  }
  
  @Test
  public void testFindModule() {
     given().headers(getAuthHeaders())
      .get("/modules/modules/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Test Module #1" ))
      .body("description", is("Module #1 for testing"))
      .body("archived", is( false ))
      .body("created", is( getDate(2010, 1, 1).toString() ) )
      .body("lastModified", is( getDate(2010, 1, 1).toString() ) )
      .body("creatorId", is(1) )
      .body("lastModifierId", is(1) )
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( 1 ))
      .body("courseModules[0].courseNumber", is( 1 ))
      .body("courseModules[0].courseLength.units", is( 1.0f ))
      .body("courseModules[0].courseLength.unit.id", is( 1 ))
      .body("maxParticipantCount", is(100) );
  }
  
  @Test
  public void testUpdateModule() {
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
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(module.getName()))
      .body("description", is(module.getDescription()))
      .body("archived", is( module.getArchived() ))
      .body("courseModules.size()", is(1))
      .body("courseModules[0].courseNumber", is(firstCourseModule(module).getCourseNumber()))
      .body("courseModules[0].subject.id", is(firstCourseModule(module).getSubject().getId().intValue()))
      .body("maxParticipantCount", is(module.getMaxParticipantCount().intValue() ) );
    
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    Long courseModuleId = response.body().jsonPath().getLong("courseModules[0].id");
    assertNotNull(id);
    
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
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateModule)
        .put("/modules/modules/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateModule.getId().intValue() ))
        .body("name", is(updateModule.getName()))
        .body("description", is(updateModule.getDescription()))
        .body("archived", is( updateModule.getArchived() ))
        .body("courseModules.size()", is(1))
        .body("courseModules[0].courseNumber", is(firstCourseModule(updateModule).getCourseNumber()))
        .body("courseModules[0].subject.id", is(firstCourseModule(updateModule).getSubject().getId().intValue()))
        .body("maxParticipantCount", is(updateModule.getMaxParticipantCount().intValue() ) );

    } finally {
      given().headers(getAuthHeaders())
        .delete("/modules/modules/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteModule() {
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
        courseModules );
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(module.getName()))
      .body("description", is(module.getDescription()))
      .body("archived", is( module.getArchived() ))
      .body("courseModules.size()", is(1))
      .body("courseModules[0].courseNumber", is(firstCourseModule(module).getCourseNumber()))
      .body("courseModules[0].subject.id", is(firstCourseModule(module).getSubject().getId().intValue()))
      .body("maxParticipantCount", is(module.getMaxParticipantCount().intValue() ) );
    
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/modules/modules/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/modules/modules/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/modules/modules/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/modules/modules/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/modules/modules/{ID}", id)
      .then()
      .statusCode(404);
  }

  @Test
  public void testListCourses() {
    Response response = given().headers(getAuthHeaders())
      .get("/modules/modules/{MODULEID}/courses", 1l);
    
    response
      .then()
      .body("id.size()", is(2))
      .body("id[0]", is(1000) )
      .body("name[0]", is("Test Course #1" ))
      .body("description[0]", is( "Course #1 for testing" ))
      .body("creatorId[0]", is( 1 ))
      .body("lastModifierId[0]", is( 1 ))
      .body("courseModules[0].size()", is( 1 ))
      .body("courseModules[0][0].subject.id", is( 1 ))
      .body("courseModules[0][0].courseNumber", is( 1 ))
      .body("courseModules[0][0].courseLength.units", is( 1.0f ))
      .body("courseModules[0][0].courseLength.unit.id", is( 1 ))
      .body("maxParticipantCount[0]", is( 100 ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(1001) )
      .body("name[1]", is("Test Course #2" ))
      .body("description[1]", is( "Course #2 for testing" ))
      .body("creatorId[1]", is( 1 ))
      .body("lastModifierId[1]", is( 1 ))
      .body("courseModules[1].size()", is( 1 ))
      .body("courseModules[1][0].subject.id", is( 1 ))
      .body("courseModules[1][0].courseNumber", is( 2 ))
      .body("courseModules[1][0].courseLength.units", is( 1.0f ))
      .body("courseModules[1][0].courseLength.unit.id", is( 1 ))
      .body("maxParticipantCount[1]", is( 200 ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testListProjects() {
    given().headers(getAuthHeaders())
      .get("/modules/modules/{MODULEID}/projects", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Test Project #1"))
      .body("description[0]", is("Project for testing"))
      .body("created[0]", is(getDate(2010, 6, 6).toString()))
      .body("lastModified[0]", is(getDate(2010, 6, 6).toString()))
      .body("creatorId[0]", is(1))
      .body("lastModifierId[0]", is(1))
      .body("optionalStudiesLength[0]", is(678f))
      .body("optionalStudiesLengthUnitId[0]", is(1))
      .body("id[1]", is(2) )
      .body("name[1]", is("Test Project #2"))
      .body("description[1]", is("Project for testing"))
      .body("created[1]", is(getDate(2011, 6, 6).toString()))
      .body("lastModified[1]", is(getDate(2011, 6, 6).toString()))
      .body("creatorId[1]", is(1))
      .body("lastModifierId[1]", is(1))
      .body("optionalStudiesLength[1]", is(910f))
      .body("optionalStudiesLengthUnitId[1]", is(1));
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

  private CourseModule firstCourseModule(Module module) {
    return module.getCourseModules().iterator().next();
  }
}
