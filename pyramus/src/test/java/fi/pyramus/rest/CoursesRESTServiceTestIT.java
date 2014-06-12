package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Course;
import fi.pyramus.rest.model.CourseLength;

public class CoursesRESTServiceTestIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourse() {
    // id, length id
    
    CourseLength length = new CourseLength(777d, 1l);
    
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
        length, 
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
      .body("length.units", is( course.getLength().getUnits().floatValue() ))
      .body("length.unitId", is( course.getLength().getUnitId().intValue() ))
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
      .body("length.units", is( 1.0f ))
      .body("length.unitId", is( 1 ))
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
      .body("length[0].units", is( 1.0f ))
      .body("length[0].unitId", is( 1 ))
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
      .body("length[1].units", is( 1.0f ))
      .body("length[1].unitId", is( 1 ))
      .body("creatorId[1]", is( 1 ))
      .body("lastModifierId[1]", is( 1 ))
      .body("subjectId[1]", is( 1 ))
      .body("maxParticipantCount[1]", is( 200 ))
      .body("archived[1]", is( false ));
  }
  
}
