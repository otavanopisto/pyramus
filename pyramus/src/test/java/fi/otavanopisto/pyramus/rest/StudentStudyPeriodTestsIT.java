package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriodType;

public class StudentStudyPeriodTestsIT extends AbstractRESTServiceTest {

  private final static long TEST_STUDENT_ID = 3l;

  @Test
  public void testCreateStudentStudyPeriod() {
    StudentStudyPeriod studyPeriod = new StudentStudyPeriod(null, TEST_STUDENT_ID, StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1), LocalDate.of(2002, 12, 31));
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyPeriod)
      .post("/students/students/{ID}/studyPeriods", TEST_STUDENT_ID);

    System.out.println(response.body().prettyPrint());
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("studentId", is(studyPeriod.getStudentId().intValue()))
      .body("type", is(studyPeriod.getType().toString()))
      .body("begin", is("2000-01-01"))
      .body("end", is("2002-12-31"))
    ;
      
    long studyPeriodId = response.body().jsonPath().getLong("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
      .then()
      .statusCode(204);
  }
  
//  @Test
//  public void testListStudentStudyPeriods() {
//    given().headers(getAuthHeaders())
//      .get("/students/students/{ID}/studyPeriods", TEST_STUDENT_ID)
//      .then()
//      .statusCode(200)
//      .body("id.size()", is(1))
//      .body("id[0]", is(3) )
//      .body("number[0]", is("+456 78 901 2345"))
//      .body("contactTypeId[0]", is(1))
//      .body("defaultNumber[0]", is(Boolean.TRUE));
//  }
  
  @Test
  public void testFindStudentStudyPeriod() {
    StudentStudyPeriod studyPeriod = new StudentStudyPeriod(null, TEST_STUDENT_ID, StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1), LocalDate.of(2002, 12, 31));
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyPeriod)
      .post("/students/students/{ID}/studyPeriods", TEST_STUDENT_ID);

    System.out.println(response.body().prettyPrint());
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("studentId", is(studyPeriod.getStudentId().intValue()))
      .body("type", is(studyPeriod.getType().toString()))
      .body("begin", is("2000-01-01"))
      .body("end", is("2002-12-31"))
    ;
      
    long studyPeriodId = response.body().jsonPath().getLong("id");

    try {
      given().headers(getAuthHeaders())
        .get("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
        .then()
        .statusCode(200)
        .body("id", not(is((Long) null)))
        .body("studentId", is(studyPeriod.getStudentId().intValue()))
        .body("type", is(studyPeriod.getType().toString()))
        .body("begin", is("2000-01-01"))
        .body("end", is("2002-12-31"))
      ;
    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
        .then()
        .statusCode(204);
    }
  }  

  @Test
  public void testUpdateStudentStudyPeriod() {
    StudentStudyPeriod studyPeriod = new StudentStudyPeriod(null, TEST_STUDENT_ID, StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1), LocalDate.of(2002, 12, 31));
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyPeriod)
      .post("/students/students/{ID}/studyPeriods", TEST_STUDENT_ID);

    System.out.println(response.body().prettyPrint());
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("studentId", is(studyPeriod.getStudentId().intValue()))
      .body("type", is(studyPeriod.getType().toString()))
      .body("begin", is("2000-01-01"))
      .body("end", is("2002-12-31"))
    ;
      
    long studyPeriodId = response.body().jsonPath().getLong("id");
    try {
      StudentStudyPeriod updatedStudyPeriod = new StudentStudyPeriod(studyPeriodId, TEST_STUDENT_ID, StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
          LocalDate.of(1980, 1, 1), LocalDate.of(1990, 12, 31));
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updatedStudyPeriod)
        .put("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
        .then()
        .statusCode(200)
        .body("id", is(updatedStudyPeriod.getId().intValue()))
        .body("studentId", is(updatedStudyPeriod.getStudentId().intValue()))
        .body("type", is(updatedStudyPeriod.getType().toString()))
        .body("begin", is("1980-01-01"))
        .body("end", is("1990-12-31"))
      ;
    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentStudyPeriod() {
    StudentStudyPeriod studyPeriod = new StudentStudyPeriod(null, TEST_STUDENT_ID, StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1), LocalDate.of(2002, 12, 31));
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyPeriod)
      .post("/students/students/{ID}/studyPeriods", TEST_STUDENT_ID);

    System.out.println(response.body().prettyPrint());
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("studentId", is(studyPeriod.getStudentId().intValue()))
      .body("type", is(studyPeriod.getType().toString()))
      .body("begin", is("2000-01-01"))
      .body("end", is("2002-12-31"))
    ;
      
    long studyPeriodId = response.body().jsonPath().getLong("id");
    assertNotNull(studyPeriodId);
    
    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
      .then()
      .statusCode(204);

    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/studyPeriods/{ID}", TEST_STUDENT_ID, studyPeriodId)
      .then()
      .statusCode(404);
  }

}
