package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.CourseAssessment;

public class CourseAssessmentTestsIT extends AbstractRESTServiceTest {

  public final long TEST_COURSESTUDENTID = 5;
  public final long TEST_GRADEID = 2;
  public final long TEST_ASSESSORID = 6;
  public final long TEST_STUDENTID = 3;
  public final long TEST_COURSEID = 1000;
  
  @Test
  public void testCreateCourseAssessment() {
    CourseAssessment courseAssessment = new CourseAssessment(null, 6l, TEST_GRADEID, 1l,TEST_ASSESSORID, getDate(2015, 1, 1), "Test assessment for test student on test course.");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", 4, TEST_COURSEID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("courseStudentId", is(courseAssessment.getCourseStudentId().intValue()))
      .body("gradeId", is( courseAssessment.getGradeId().intValue() ))
      .body("assessorId", is( courseAssessment.getAssessorId().intValue() ))
      .body("date", is( courseAssessment.getDate().toString() ))
      .body("verbalAssessment", is( courseAssessment.getVerbalAssessment() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listCourseAssessments() {
    given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, TEST_COURSEID )
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1) )
      .body("courseStudentId[0]", is((int) TEST_COURSESTUDENTID ))
      .body("gradeId[0]", is((int) TEST_GRADEID))
      .body("assessorId[0]", is( 6 ))
      .body("verbalAssessment[0]", is( "TEST ASSESSMENT" ));
  }
  
  @Test
  public void testFindCourseAssessment() {
    given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", TEST_STUDENTID, TEST_COURSEID, 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("courseStudentId", is(5))
      .body("gradeId", is( 2 ))
      .body("assessorId", is( 6 ))
      .body("verbalAssessment", is( "TEST ASSESSMENT" ));
  }
  
  @Test
  public void testUpdateCourseAssessment() {
    CourseAssessment courseAssessment = new CourseAssessment(null, 6l, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Not Updated.");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", 4, TEST_COURSEID);

    response.then()
      .body("id", not(is((Long) null)))
      .body("courseStudentId", is(courseAssessment.getCourseStudentId().intValue()))
      .body("gradeId", is( courseAssessment.getGradeId().intValue() ))
      .body("assessorId", is( courseAssessment.getAssessorId().intValue() ))
      .body("date", is( courseAssessment.getDate().toString() ))
      .body("verbalAssessment", is( courseAssessment.getVerbalAssessment() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      CourseAssessment updatedCourseAssessment = new CourseAssessment(id, 6l, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 2, 1), "Updated");

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updatedCourseAssessment)
        .put("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, id)
        .then()
        .statusCode(200)
        .body("id", not(is((Long) null)))
        .body("courseStudentId", is(updatedCourseAssessment.getCourseStudentId().intValue()))
        .body("gradeId", is( updatedCourseAssessment.getGradeId().intValue() ))
        .body("assessorId", is( updatedCourseAssessment.getAssessorId().intValue() ))
        .body("date", is( updatedCourseAssessment.getDate().toString() ))
        .body("verbalAssessment", is( updatedCourseAssessment.getVerbalAssessment() ));

    } finally {
      given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, id)
      .then()
      .statusCode(204);
    }
  }
}
