package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.allOf;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Course;

public class CourseTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourse() {
    Course course = new Course(
        "Create test", 
        new Date(), 
        new Date(), 
        "Course for testing course creation", 
        Boolean.FALSE, 
        111, 
        222l,
        new Date(),
        new Date(),
        "Extension",
        333d,
        444d,
        555d,
        666d,
        777d,
        new Date(),
        1l, 
        1l,
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null);

    Response response = given()
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(course.getName()))
      .body("courseNumber", is( course.getCourseNumber()))
      .body("description", is( course.getDescription() ))
      .body("length", is( course.getLength().floatValue() ))
      .body("lengthUnitId", is( course.getLengthUnitId().intValue() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("subjectId", is( course.getSubjectId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))
      .body("archived", is( course.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testCreateCourseTags() {
    Course course = new Course(
        "Create test", 
        new Date(), 
        new Date(), 
        "Course for testing course creation", 
        Boolean.FALSE, 
        111, 
        222l,
        new Date(),
        new Date(),
        "Extension",
        333d,
        444d,
        555d,
        666d,
        777d,
        new Date(),
        1l, 
        1l,
        1l,
        777d,
        1l,
        1l, 
        1l, 
        Arrays.asList("tag1", "tag2", "tag3"));

    Response response = given()
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(course.getName()))
      .body("courseNumber", is( course.getCourseNumber()))
      .body("description", is( course.getDescription() ))
      .body("length", is( course.getLength().floatValue() ))
      .body("lengthUnitId", is( course.getLengthUnitId().intValue() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("subjectId", is( course.getSubjectId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))
      .body("tags.size()", is(3))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2"), hasItem("tag3")))
      .body("archived", is( course.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testGetCourse() {
    given()
      .get("/courses/courses/1001")
      .then()
      .body("id", is(1001))
      .body("name", is("Test Course #2" ))
      .body("courseNumber", is( 2 ))
      .body("created", is( 1293832800000l ))
      .body("lastModified", is( 1293832800000l ))
      .body("description", is( "Course #2 for testing" ))
      .body("length", is( 1.0f ))
      .body("lengthUnitId", is( 1 ))
      .body("creatorId", is( 1 ))
      .body("lastModifierId", is( 1 ))
      .body("subjectId", is( 1 ))
      .body("maxParticipantCount", is( 200 ))
      .body("archived", is( false ));
  }

  @Test
  public void testGetCourseNotFound() {
    given()
      .get("/courses/courses/bogus")
      .then()
      .statusCode(404);
    
    given()
      .get("/courses/courses/12345")
      .then()
      .statusCode(404);
    
    given()
      .get("/courses/courses/-12356")
      .then()
      .statusCode(404);
    
    given()
      .get("/courses/courses/לְנַסוֹת")
      .then()
      .statusCode(404);
  }
 
  @Test
  public void testListCourses() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
    given()
      .get("/courses/courses/")
      .then()
      .body("id.size()", is(2))
      .body("id[0]", is(1000) )
      .body("name[0]", is("Test Course #1" ))
      .body("courseNumber[0]", is( 1 ))
      .body("created[0]", is( 1262296800000l ))
      .body("lastModified[0]", is( 1262296800000l ))
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
      .body("created[1]", is( 1293832800000l ))
      .body("lastModified[1]", is( 1293832800000l ))
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
  public void testUpdateCourse() {
    Course course = new Course(
        "Update test", 
        new Date(), 
        new Date(), 
        "Course for testing course updating", 
        Boolean.FALSE, 
        111, 
        222l,
        new Date(),
        new Date(),
        "Extension",
        333d,
        444d,
        555d,
        666d,
        777d,
        new Date(),
        1l, 
        1l,
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null);

    Response response = given()
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(course.getName()))
      .body("courseNumber", is( course.getCourseNumber()))
      .body("description", is( course.getDescription() ))
      .body("length", is( course.getLength().floatValue() ))
      .body("lengthUnitId", is( course.getLengthUnitId().intValue() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("subjectId", is( course.getSubjectId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))
      .body("archived", is( course.getArchived() ));
      
    course.setId(new Long(response.body().jsonPath().getInt("id")));
    course.setName("Updated name");
    course.setCourseNumber(999);
    course.setDescription("Updated description");
    course.setLength(888d);
    course.setMaxParticipantCount(1234l);
    
    given()
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId())
      .then()
      .statusCode(200)
      .body("id", is(course.getId().intValue()))
      .body("name", is(course.getName()))
      .body("courseNumber", is( course.getCourseNumber()))
      .body("description", is( course.getDescription() ))
      .body("length", is( course.getLength().floatValue() ))
      .body("lengthUnitId", is( course.getLengthUnitId().intValue() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("subjectId", is( course.getSubjectId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))
      .body("archived", is( course.getArchived() ));
    
    given()
      .delete("/courses/courses/{ID}?permanent=true", course.getId())
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testUpdateCourseTags() {
    Course course = new Course(
        "Update test", 
        new Date(), 
        new Date(), 
        "Course for testing course updating", 
        Boolean.FALSE, 
        111, 
        222l,
        new Date(),
        new Date(),
        "Extension",
        333d,
        444d,
        555d,
        666d,
        777d,
        new Date(),
        1l, 
        1l,
        1l,
        777d,
        1l,
        1l, 
        1l, 
        Arrays.asList("tag1", "tag2", "tag3"));

    Response response = given()
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(course.getName()))
      .body("courseNumber", is( course.getCourseNumber()))
      .body("description", is( course.getDescription() ))
      .body("length", is( course.getLength().floatValue() ))
      .body("lengthUnitId", is( course.getLengthUnitId().intValue() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("subjectId", is( course.getSubjectId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))
      .body("tags.size()", is(3))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2"), hasItem("tag3")))
      .body("archived", is( course.getArchived() ));
      
    course.setId(new Long(response.body().jsonPath().getInt("id")));
    course.setName("Updated name");
    course.setCourseNumber(999);
    course.setDescription("Updated description");
    course.setLength(888d);
    course.setMaxParticipantCount(1234l);
    course.setTags(Arrays.asList("tag1", "tag3", "tag4", "tag5"));
    
    given()
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId())
      .then()
      .statusCode(200)
      .body("id", is(course.getId().intValue()))
      .body("name", is(course.getName()))
      .body("courseNumber", is( course.getCourseNumber()))
      .body("description", is( course.getDescription() ))
      .body("length", is( course.getLength().floatValue() ))
      .body("lengthUnitId", is( course.getLengthUnitId().intValue() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("subjectId", is( course.getSubjectId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))      
      .body("tags.size()", is(4))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag3"), hasItem("tag4"), hasItem("tag5")))
      .body("archived", is( course.getArchived() ));
    
    given()
      .delete("/courses/courses/{ID}?permanent=true", course.getId())
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourse() {
    Course course = new Course(
        "Update test", 
        new Date(), 
        new Date(), 
        "Course for testing course updating", 
        Boolean.FALSE, 
        111, 
        222l,
        new Date(),
        new Date(),
        "Extension",
        333d,
        444d,
        555d,
        666d,
        777d,
        new Date(),
        1l, 
        1l,
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null);

    Response response = given()
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/courses/courses/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/courses/courses/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/courses/courses/{ID}", id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().get("/courses/courses/{ID}", id)
      .then()
      .statusCode(404);
  }
  
}
