package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.joda.time.DateTime;
import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.ContactType;
import fi.pyramus.rest.model.CourseAssessment;

public class CourseAssessmentTestsIT extends AbstractRESTServiceTest {

  public final long TEST_COURSESTUDENTID = 3;
  public final long TEST_GRADEID = 2;
  public final long TEST_ASSESSORID = 6;
  public final long TEST_STUDENTID = 3;
  public final long TEST_COURSEID = 1001;
  
  @Test
  public void testCreateCourseAssessment() {
    CourseAssessment courseAssessment = new CourseAssessment(null, TEST_COURSESTUDENTID, TEST_GRADEID, TEST_ASSESSORID, new DateTime(), "Test assessment for test student on test course.");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments/", TEST_STUDENTID, TEST_COURSEID);

    response.then()
      .body("id", not(is((Long) null)))
      .body("courseStudentId", is(courseAssessment.getCourseStudentId()))
      .body("gradeId", is( courseAssessment.getGradeId() ))
      .body("assessorId", is( courseAssessment.getAssessorId() ))
      .body("date", is( courseAssessment.getDate() ))
      .body("verbalAssessment", is( courseAssessment.getVerbalAssessment() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments/{ID}",TEST_STUDENTID, TEST_COURSEID, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listCourseAssessments() {
    given().headers(getAuthHeaders())
      .get("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments", 4, TEST_COURSEID )
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("courseStudentId[0]", is(4))
      .body("gradeId[0]", is( 2 ))
      .body("assessorId[0]", is( 6 ))
      .body("verbalAssessment[0]", is( "TEST ASSESSMENT" ));
  }
  
  @Test
  public void testFindCourseAssessment() {
    given().headers(getAuthHeaders())
      .get("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("courseStudentId", is(4))
      .body("gradeId", is( 2 ))
      .body("assessorId", is( 6 ))
      .body("verbalAssessment", is( "TEST ASSESSMENT" ));
  }
  
  @Test
  public void testUpdateCourseAssessment() {
    CourseAssessment courseAssessment = new CourseAssessment(null, TEST_COURSESTUDENTID, TEST_GRADEID, TEST_ASSESSORID, new DateTime(), "Test assessment for test student on test course.");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments/", TEST_STUDENTID, TEST_COURSEID);

    response.then()
      .body("id", not(is((Long) null)))
      .body("courseStudentId", is(courseAssessment.getCourseStudentId()))
      .body("gradeId", is( courseAssessment.getGradeId() ))
      .body("assessorId", is( courseAssessment.getAssessorId() ))
      .body("date", is( courseAssessment.getDate() ))
      .body("verbalAssessment", is( courseAssessment.getVerbalAssessment() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      CourseAssessment updatedCourseAssessment = new CourseAssessment(id, TEST_COURSESTUDENTID, TEST_GRADEID, TEST_ASSESSORID, new DateTime(), "Updated");

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updatedCourseAssessment)
        .put("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments/{ID}",TEST_STUDENTID, TEST_COURSEID, id)
        .then()
        .statusCode(200)
        .body("id", not(is((Long) null)))
        .body("courseStudentId", is(updatedCourseAssessment.getCourseStudentId()))
        .body("gradeId", is( updatedCourseAssessment.getGradeId() ))
        .body("assessorId", is( updatedCourseAssessment.getAssessorId() ))
        .body("date", is( updatedCourseAssessment.getDate() ))
        .body("verbalAssessment", is( updatedCourseAssessment.getVerbalAssessment() ));

    } finally {
      given().headers(getAuthHeaders())
      .delete("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments/{ID}",TEST_STUDENTID, TEST_COURSEID, id)
      .then()
      .statusCode(204);
    }
  }
}
