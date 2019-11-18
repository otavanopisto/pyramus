package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.time.OffsetDateTime;
import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Course;

public class CourseTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourse() {
    Course course = createCourse(
        "Create test", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "Course for testing course creation", 
        Boolean.FALSE, 
        111, 
        222l,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        "Extension",
        333d,
        444d,
        468d,
        555d,
        666d,
        777d,
        OffsetDateTime.now(),
        getUserId(), 
        getUserId(),
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null,
        null,
        null,
        1L);

    Response response = given().headers(getAuthHeaders())
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
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testCreateCourseTags() {
    Course course = createCourse(
        "Create test", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "Course for testing course creation", 
        Boolean.FALSE, 
        111, 
        222l,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        "Extension",
        333d,
        444d,
        468d,
        555d,
        666d,
        777d,
        OffsetDateTime.now(),
        getUserId(), 
        getUserId(),
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null,
        null,
        Arrays.asList("tag1", "tag2", "tag3"),
        1L);

    Response response = given().headers(getAuthHeaders())
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
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testGetCourse() {
    OffsetDateTime created = getDate(2011, 1, 1);
    OffsetDateTime modified = getDate(2011, 1, 1);
    OffsetDateTime beginDate = getDateToOffsetDateTime(2011, 2, 2);
    OffsetDateTime endDate = getDateToOffsetDateTime(2011, 3, 3);
    OffsetDateTime enrolmentTimeEnd = getDate(2011, 1, 1);

    given().headers(getAuthHeaders())
      .get("/courses/courses/1001")
      .then()
      .body("id", is(1001))
      .body("name", is("Test Course #2" ))
      .body("courseNumber", is( 2 ))
      .body("created", is( created.toString() ))
      .body("lastModified", is( modified.toString() ))
      .body("beginDate", is( beginDate.toString() ))
      .body("endDate", is( endDate.toString() ))
      .body("enrolmentTimeEnd", is( enrolmentTimeEnd.toString() ))
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
    given().headers(getAuthHeaders())
      .get("/courses/courses/bogus")
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .get("/courses/courses/12345")
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .get("/courses/courses/-12356")
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .get("/courses/courses/לְנַסוֹת")
      .then()
      .statusCode(404);
  }
 
  @Test
  public void testListCourses() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
    OffsetDateTime created1 = getDate(2010, 1, 1);
    OffsetDateTime modified1 = getDate(2010, 1, 1);
    OffsetDateTime beginDate1 = getDateToOffsetDateTime(2010, 2, 2);
    OffsetDateTime endDate1 = getDateToOffsetDateTime(2010, 3, 3);
    OffsetDateTime enrolmentTimeEnd1 = getDate(2010, 1, 1);
    
    OffsetDateTime created2 = getDate(2011, 1, 1);
    OffsetDateTime modified2 = getDate(2011, 1, 1);
    OffsetDateTime beginDate2 = getDateToOffsetDateTime(2011, 2, 2);
    OffsetDateTime endDate2 = getDateToOffsetDateTime(2011, 3, 3);
    OffsetDateTime enrolmentTimeEnd2 = getDate(2011, 1, 1);

    given().headers(getAuthHeaders())
      .get("/courses/courses/")
      .then()
      .body("id.size()", is(2))
      .body("id[0]", is(1000) )
      .body("name[0]", is("Test Course #1" ))
      .body("courseNumber[0]", is( 1 ))
      .body("created[0]", is( created1.toString() ))
      .body("lastModified[0]", is( modified1.toString() ))
      .body("beginDate[0]", is( beginDate1.toString() ))
      .body("endDate[0]", is( endDate1.toString() ))
      .body("enrolmentTimeEnd[0]", is( enrolmentTimeEnd1.toString() ))
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
      .body("created[1]", is( created2.toString() ))
      .body("lastModified[1]", is( modified2.toString() ))
      .body("beginDate[1]", is( beginDate2.toString() ))
      .body("endDate[1]", is( endDate2.toString() ))
      .body("enrolmentTimeEnd[1]", is( enrolmentTimeEnd2.toString() ))
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
    Course course = createCourse(
        "Update test", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "Course for testing course updating", 
        Boolean.FALSE, 
        111, 
        222l,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        "Extension",
        333d,
        444d,
        468d,
        555d,
        666d,
        777d,
        OffsetDateTime.now(),
        getUserId(), 
        getUserId(),
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null,
        null,
        null,
        1L);

    Response response = given().headers(getAuthHeaders())
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
    
    given().headers(getAuthHeaders())
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
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", course.getId())
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testUpdateCourseTags() {
    Course course = createCourse(
        "Update test", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "Course for testing course updating", 
        Boolean.FALSE, 
        111, 
        222l,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        "Extension",
        333d,
        444d,
        468d,
        555d,
        666d,
        777d,
        OffsetDateTime.now(),
        getUserId(), 
        getUserId(),
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null,
        null,
        Arrays.asList("tag1", "tag2", "tag3"),
        1L);

    Response response = given().headers(getAuthHeaders())
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
    
    given().headers(getAuthHeaders())
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
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", course.getId())
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourse() {
    Course course = createCourse(
        "Update test", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "Course for testing course updating", 
        Boolean.FALSE, 
        111, 
        222l,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        "Extension",
        333d,
        444d,
        468d,
        555d,
        666d,
        777d,
        OffsetDateTime.now(),
        getUserId(), 
        getUserId(),
        1l,
        777d,
        1l,
        1l, 
        1l, 
        null,
        null,
        null,
        1L);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/courses/courses/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/courses/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/courses/{ID}", id)
      .then()
      .statusCode(404);
  }
  
  private Course createCourse(String name, OffsetDateTime created, OffsetDateTime lastModified, String description, Boolean archived, Integer courseNumber, 
      Long maxParticipantCount, OffsetDateTime beginDate, OffsetDateTime endDate, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingHours, Double distanceTeachingDays, Double assessingHours, Double planningHours, OffsetDateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Double length, Long lengthUnitId, Long moduleId, Long stateId, Long typeId, 
      Map<String, String> variables, List<String> tags, Long organizationId) {
    return new Course(null, name, created, lastModified, description, archived, courseNumber, maxParticipantCount, beginDate, endDate, 
        nameExtension, localTeachingDays, teachingHours, distanceTeachingHours, distanceTeachingDays, assessingHours, planningHours, enrolmentTimeEnd, 
        creatorId, lastModifierId, subjectId, null, length, lengthUnitId, moduleId, stateId, typeId, variables, tags, organizationId);
  }
  
}
