package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.time.OffsetDateTime;
import org.junit.Test;

public class StudentCoursesTestsIT extends AbstractRESTServiceTest {

  private final static long TEST_STUDENT_ID = 3l;
  
  @Test
  public void testStudentListCourses() {
    OffsetDateTime created1 = getDate(2010, 1, 1);
    OffsetDateTime modified1 = getDate(2010, 1, 1);
    OffsetDateTime beginDate1 = getDateToOffsetDateTime(2010, 2, 2);
    OffsetDateTime endDate1 = getDateToOffsetDateTime(2010, 3, 3);
    OffsetDateTime enrolmentTimeEnd1 = getDate(2010, 1, 1);
    given().headers(getAuthHeaders())
      .get("/students/students/{ID}/courses", TEST_STUDENT_ID)
      .then()
      .body("id.size()", is(1))
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
      .body("archived[0]", is( false ));
  }
}
