package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.junit.Test;

public class CoursesRESTServiceTestIT extends AbstractRESTServiceTest {

//CourseRESTServiceTest Course
//  
// @Test
// public void testCreateCourse() throws ClientProtocolException, IOException, ParseException {
//   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//   Date beginDate = sdf.parse("2013-02-13");
//   Date endDate = sdf.parse("2013-05-09");
//   Date enrolmentTimeEnd = sdf.parse("2013-01-31");
//   CourseEntity courseEntity = new CourseEntity();
//   courseEntity.setModule_id(1l);
//   courseEntity.setName("Test course");
//   courseEntity.setNameExtension("Test extension");
//   courseEntity.setState_id(1l);
//   courseEntity.setSubject_id(1l);
//   courseEntity.setCourseNumber(2);
//   courseEntity.setBeginDate(beginDate);
//   courseEntity.setEndDate(endDate);
//   courseEntity.setCourseLength_id(1l);
//   courseEntity.setCourseLength(15d);
//   courseEntity.setDistanceTeachingDays(10d);
//   courseEntity.setLocalTeachingDays(5d);
//   courseEntity.setTeachingHours(60d);
//   courseEntity.setPlanningHours(15d);
//   courseEntity.setAssessingHours(5d);
//   courseEntity.setDescription("Testing how courses work");
//   courseEntity.setMaxParticipantCount(25l);
//   courseEntity.setEnrolmentTimeEnd(enrolmentTimeEnd);
// 
//   HttpResponse response = doPostRequest("/courses/courses/", courseEntity);
// 
//   assertEquals(200, response.getStatusLine().getStatusCode());
//   HttpEntity entity = response.getEntity();
//   try {
//     assertNotNull(entity);
//     assertEquals("application/json", entity.getContentType().getValue());
//     courseEntity = unserializeEntity(CourseEntity.class, EntityUtils.toString(entity));
//     assertNotNull(courseEntity);
//     assertEquals((Long) 2l, courseEntity.getId());
//     assertEquals("Test course", courseEntity.getName());
//     assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-02-13T00:00:00").getTime(), courseEntity.getBeginDate());
//     assertEquals("Testing how courses work", courseEntity.getDescription());
//   } finally {
//     EntityUtils.consume(entity);
//   }
// }
//  

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
      .body("subject.id", is( 1 ))
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
      .body("subject[0].id", is( 1 ))
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
      .body("subject[1].id", is( 1 ))
      .body("maxParticipantCount[1]", is( 200 ))
      .body("archived[1]", is( false ));
  }
  
}
