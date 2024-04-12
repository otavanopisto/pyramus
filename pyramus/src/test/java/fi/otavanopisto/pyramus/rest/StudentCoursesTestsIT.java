package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class StudentCoursesTestsIT extends AbstractRESTServiceTest {

  private final static long TEST_STUDENT_ID = 3l;
  
  @Test
  public void testStudentListCourses() {
    String created1 = getDateString(2010, 1, 1);
    String modified1 = getDateString(2010, 1, 1);
    String beginDate1 = getDateString(2010, 2, 2);
    String endDate1 = getDateString(2010, 3, 3);
    String enrolmentTimeEnd1 = getDateString(2010, 1, 1);
    given().headers(getAuthHeaders())
      .get("/students/students/{ID}/courses", TEST_STUDENT_ID)
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
      .body("courseModules[0][0].subject.id", is( 1 ))
      .body("courseModules[0][0].courseNumber", is( 1 ))
      .body("courseModules[0][0].courseLength.units", is( 1.0f ))
      .body("courseModules[0][0].courseLength.unit.id", is( 1 ))
      .body("maxParticipantCount[0]", is( 100 ))
      .body("archived[0]", is( false ));
  }
}
