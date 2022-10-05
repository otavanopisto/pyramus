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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.OffsetDateTime;
import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Course;
import fi.otavanopisto.pyramus.rest.model.CourseLength;
import fi.otavanopisto.pyramus.rest.model.CourseModule;
import fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit;
import fi.otavanopisto.pyramus.rest.model.Subject;

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
      .body("description", is( course.getDescription() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( firstCourseModule(course).getSubject().getId().intValue() ))
      .body("courseModules[0].courseNumber", is( firstCourseModule(course).getCourseNumber()))
      .body("courseModules[0].courseLength.units", is( firstCourseModule(course).getCourseLength().getUnits().floatValue() ))
      .body("courseModules[0].courseLength.unit.id", is( firstCourseModule(course).getCourseLength().getUnit().getId().intValue() ))
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
      .body("description", is( course.getDescription() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( firstCourseModule(course).getSubject().getId().intValue() ))
      .body("courseModules[0].courseNumber", is( firstCourseModule(course).getCourseNumber()))
      .body("courseModules[0].courseLength.units", is( firstCourseModule(course).getCourseLength().getUnits().floatValue() ))
      .body("courseModules[0].courseLength.unit.id", is( firstCourseModule(course).getCourseLength().getUnit().getId().intValue() ))
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
    String created = getDateString(2011, 1, 1);
    String modified = getDateString(2011, 1, 1);
    String beginDate = getDateString(2011, 2, 2);
    String endDate = getDateString(2011, 3, 3);
    String enrolmentTimeEnd = getDateString(2011, 1, 1);

    given().headers(getAuthHeaders())
      .get("/courses/courses/1001")
      .then()
      .body("id", is(1001))
      .body("name", is("Test Course #2" ))
      .body("created", is( created ))
      .body("lastModified", is( modified ))
      .body("beginDate", is( beginDate ))
      .body("endDate", is( endDate ))
      .body("enrolmentTimeEnd", is( enrolmentTimeEnd ))
      .body("description", is( "Course #2 for testing" ))
      .body("creatorId", is( 1 ))
      .body("lastModifierId", is( 1 ))
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( 1 ))
      .body("courseModules[0].courseNumber", is( 2 ))
      .body("courseModules[0].courseLength.units", is( 1.0f ))
      .body("courseModules[0].courseLength.unit.id", is( 1 ))
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
    String created1 = getDateString(2010, 1, 1);
    String modified1 = getDateString(2010, 1, 1);
    String beginDate1 = getDateString(2010, 2, 2);
    String endDate1 = getDateString(2010, 3, 3);
    String enrolmentTimeEnd1 = getDateString(2010, 1, 1);
    
    String created2 = getDateString(2011, 1, 1);
    String modified2 = getDateString(2011, 1, 1);
    String beginDate2 = getDateString(2011, 2, 2);
    String endDate2 = getDateString(2011, 3, 3);
    String enrolmentTimeEnd2 = getDateString(2011, 1, 1);

    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/");
    
    response
      .then()
      .body("id.size()", is(2))
      .body("id[0]", is(1000) )
      .body("name[0]", is("Test Course #1" ))
      .body("created[0]", is( created1 ))
      .body("lastModified[0]", is( modified1 ))
      .body("beginDate[0]", is( beginDate1 ))
      .body("endDate[0]", is( endDate1 ))
      .body("enrolmentTimeEnd[0]", is( enrolmentTimeEnd1 ))
      .body("description[0]", is( "Course #1 for testing" ))
      .body("creatorId[0]", is( 1 ))
      .body("lastModifierId[0]", is( 1 ))
      .body("courseModules[0].size()", is( 1 ))
      .body("courseModules[0][0].courseNumber", is( 1 ))
      .body("courseModules[0][0].courseLength.units", is( 1.0f ))
      .body("courseModules[0][0].courseLength.unit.id", is( 1 ))
      .body("courseModules[0][0].subject.id", is( 1 ))
      .body("maxParticipantCount[0]", is( 100 ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(1001) )
      .body("name[1]", is("Test Course #2" ))
      .body("created[1]", is( created2 ))
      .body("lastModified[1]", is( modified2 ))
      .body("beginDate[1]", is( beginDate2 ))
      .body("endDate[1]", is( endDate2 ))
      .body("enrolmentTimeEnd[1]", is( enrolmentTimeEnd2 ))
      .body("description[1]", is( "Course #2 for testing" ))
      .body("creatorId[1]", is( 1 ))
      .body("lastModifierId[1]", is( 1 ))
      .body("courseModules[1].size()", is( 1 ))
      .body("courseModules[1][0].courseNumber", is( 2 ))
      .body("courseModules[1][0].courseLength.units", is( 1.0f ))
      .body("courseModules[1][0].courseLength.unit.id", is( 1 ))
      .body("courseModules[1][0].subject.id", is( 1 ))
      .body("maxParticipantCount[1]", is( 200 ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testUpdateCourse() {
    Course course = createCourse(
        "Update test - testUpdateCourse()", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "Course for testing course updating - testUpdateCourse()", 
        Boolean.FALSE, 
        111, 
        222l,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
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
      .body("description", is( course.getDescription() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( firstCourseModule(course).getSubject().getId().intValue() ))
      .body("courseModules[0].courseNumber", is( firstCourseModule(course).getCourseNumber()))
      .body("courseModules[0].courseLength.units", is( firstCourseModule(course).getCourseLength().getUnits().floatValue() ))
      .body("courseModules[0].courseLength.unit.id", is( firstCourseModule(course).getCourseLength().getUnit().getId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))
      .body("archived", is( course.getArchived() ));
      
    course.setId(response.body().jsonPath().getLong("id"));
    course.setName("Updated name - testUpdateCourse()");
    course.setDescription("Updated description - testUpdateCourse()");
    course.setMaxParticipantCount(1234l);
    
    firstCourseModule(course).setId(response.body().jsonPath().getLong("courseModules[0].id"));
    firstCourseModule(course).setCourseNumber(999);
    firstCourseModule(course).getCourseLength().setUnits(888d);

    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId())
      .then()
      .statusCode(200)
      .body("id", is(course.getId().intValue()))
      .body("name", is(course.getName()))
      .body("description", is( course.getDescription() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( firstCourseModule(course).getSubject().getId().intValue() ))
      .body("courseModules[0].courseNumber", is( firstCourseModule(course).getCourseNumber()))
      .body("courseModules[0].courseLength.units", is( firstCourseModule(course).getCourseLength().getUnits().floatValue() ))
      .body("courseModules[0].courseLength.unit.id", is( firstCourseModule(course).getCourseLength().getUnit().getId().intValue() ))
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
        "Update test - testUpdateCourseTags()", 
        OffsetDateTime.now(), 
        OffsetDateTime.now(), 
        "Course for testing course updating - testUpdateCourseTags()", 
        Boolean.FALSE, 
        111, 
        222l,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
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
      .body("description", is( course.getDescription() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( firstCourseModule(course).getSubject().getId().intValue() ))
      .body("courseModules[0].courseNumber", is( firstCourseModule(course).getCourseNumber()))
      .body("courseModules[0].courseLength.units", is( firstCourseModule(course).getCourseLength().getUnits().floatValue() ))
      .body("courseModules[0].courseLength.unit.id", is( firstCourseModule(course).getCourseLength().getUnit().getId().intValue() ))
      .body("maxParticipantCount", is( course.getMaxParticipantCount().intValue() ))
      .body("tags.size()", is(3))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2"), hasItem("tag3")))
      .body("archived", is( course.getArchived() ));
      
    course.setId(response.body().jsonPath().getLong("id"));
    course.setName("Updated name - testUpdateCourseTags()");
    course.setDescription("Updated description - testUpdateCourseTags()");
    course.setMaxParticipantCount(1234l);
    course.setTags(Arrays.asList("tag1", "tag3", "tag4", "tag5"));

    firstCourseModule(course).setId(response.body().jsonPath().getLong("courseModules[0].id"));
    firstCourseModule(course).setCourseNumber(999);
    firstCourseModule(course).getCourseLength().setUnits(888d);

    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId())
      .then()
      .statusCode(200)
      .body("id", is(course.getId().intValue()))
      .body("name", is(course.getName()))
      .body("description", is( course.getDescription() ))
      .body("creatorId", is( course.getCreatorId().intValue() ))
      .body("lastModifierId", is( course.getLastModifierId().intValue() ))
      .body("courseModules.size()", is( 1 ))
      .body("courseModules[0].subject.id", is( firstCourseModule(course).getSubject().getId().intValue() ))
      .body("courseModules[0].courseNumber", is( firstCourseModule(course).getCourseNumber()))
      .body("courseModules[0].courseLength.units", is( firstCourseModule(course).getCourseLength().getUnits().floatValue() ))
      .body("courseModules[0].courseLength.unit.id", is( firstCourseModule(course).getCourseLength().getUnit().getId().intValue() ))
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
    
    Long id = response.body().jsonPath().getLong("id");
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
      Long maxParticipantCount, OffsetDateTime beginDate, OffsetDateTime endDate, OffsetDateTime signupStart, OffsetDateTime signupEnd, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingHours, Double distanceTeachingDays, Double assessingHours, Double planningHours, OffsetDateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Double length, Long lengthUnitId, Long moduleId, Long stateId, Long typeId, 
      Map<String, String> variables, List<String> tags, Long organizationId) {
    
    CourseLength courseLength = new CourseLength(null, null, length, new EducationalTimeUnit(lengthUnitId, null, null, null, false));
    CourseModule courseModule = new CourseModule(null, new Subject(subjectId, null, null, null, false), courseNumber, courseLength);
    Set<CourseModule> courseModules = new HashSet<>(Arrays.asList(courseModule));

    return new Course(null, name, created, lastModified, description, archived, maxParticipantCount, beginDate, endDate, signupStart, signupEnd,
        nameExtension, localTeachingDays, teachingHours, distanceTeachingHours, distanceTeachingDays, assessingHours, planningHours, enrolmentTimeEnd, 
        creatorId, lastModifierId, null, moduleId, stateId, typeId, variables, tags, organizationId, false, null, null, courseModules);
  }

  private CourseModule firstCourseModule(Course course) {
    return course.getCourseModules().iterator().next();
  }
}
