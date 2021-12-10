package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.time.OffsetDateTime;
import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Module;

public class ModuleTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateModule() {
    Module module = new Module(null,
        "Create test", 
        null, 
        null, 
        "Course for testing course creation", 
        Boolean.FALSE, 
        111, 
        222l,
        null,
        null,
        1l,
        null,
        1d, 
        1l, 
        null);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules/");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(module.getName()))
      .body("courseNumber", is( module.getCourseNumber()))
      .body("description", is( module.getDescription() ))
      .body("length", is( module.getLength().floatValue() ))
      .body("lengthUnitId", is( module.getLengthUnitId().intValue() ))
      .body("creatorId", not(is((Long) null )))
      .body("lastModifierId", not(is((Long) null )))
      .body("created", not(is((String) null )))
      .body("lastModified", not(is((String) null )))
      .body("subjectId", is( module.getSubjectId().intValue() ))
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
      .body("courseNumber", is(1) )
      .body("created", is( getDate(2010, 1, 1).toString() ) )
      .body("lastModified", is( getDate(2010, 1, 1).toString() ) )
      .body("creatorId", is(1) )
      .body("lastModifierId", is(1) )
      .body("subjectId", is(1) )
      .body("maxParticipantCount", is(100) );
  }
  
  @Test
  public void testUpdateModule() {
    Module module = new Module(null,
        "not updated", 
        null, 
        null, 
        "not updated module", 
        Boolean.FALSE, 
        111, 
        222l,
        null,
        null,
        1l,
        null,
        1d, 
        1l, 
        null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(module.getName()))
      .body("description", is(module.getDescription()))
      .body("archived", is( module.getArchived() ))
      .body("courseNumber", is(module.getCourseNumber()) )
      .body("subjectId", is(module.getSubjectId().intValue()) )
      .body("maxParticipantCount", is(module.getMaxParticipantCount().intValue() ) );
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    try {
      Module updateModule = new Module(id,
          "updated", 
          null, 
          null, 
          "updated module", 
          Boolean.FALSE, 
          222, 
          333l,
          null,
          null,
          2l,
          null,
          2d, 
          2l, 
          null);
      
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
        .body("courseNumber", is(updateModule.getCourseNumber()) )
        .body("subjectId", is(updateModule.getSubjectId().intValue()) )
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
    Module module = new Module(null,
        "not updated", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "not updated module", 
        Boolean.FALSE, 
        111, 
        222l,
        1l,
        1l,
        1l,
        null,
        1d, 
        1l, 
        null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(module)
      .post("/modules/modules");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(module.getName()))
      .body("description", is(module.getDescription()))
      .body("archived", is( module.getArchived() ))
      .body("courseNumber", is(module.getCourseNumber()) )
      .body("subjectId", is(module.getSubjectId().intValue()) )
      .body("maxParticipantCount", is(module.getMaxParticipantCount().intValue() ) );
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
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
    given().headers(getAuthHeaders())
      .get("/modules/modules/{MODULEID}/courses", 1l)
      .then()
      .body("id.size()", is(2))
      .body("id[0]", is(1000) )
      .body("name[0]", is("Test Course #1" ))
      .body("courseNumber[0]", is( 1 ))
      .body("description[0]", is( "Course #1 for testing" ))
      .body("length[0]", is( 1.0f ))
      .body("lengthUnitId[0]", is( 1 ))
      .body("creatorId[0]", is( 1 ))
      .body("lastModifierId[0]", is( 1 ))
      .body("subjectId[0]", is( 1 ))
      .body("maxParticipantCount[0]", is( 100 ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(1001) )
      .body("name[1]", is("Test Course #2" ))
      .body("courseNumber[1]", is( 2 ))
      .body("description[1]", is( "Course #2 for testing" ))
      .body("length[1]", is( 1.0f ))
      .body("lengthUnitId[1]", is( 1 ))
      .body("creatorId[1]", is( 1 ))
      .body("lastModifierId[1]", is( 1 ))
      .body("subjectId[1]", is( 1 ))
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
}
