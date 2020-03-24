package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.restassured.response.Response;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriodType;


/**
 * Tests of permissions used in StudentRESTService
 */
@TestInstance(Lifecycle.PER_CLASS)
public class StudentStudyPeriodPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private static final long TEST_STUDENT_ID = 13L;
  
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @BeforeAll
  public void beforeTests() {
  }
  
  @AfterAll
  public void after() {
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudentStudyPeriod(Role role) throws NoSuchFieldException {
    StudentStudyPeriod studentStudyPeriod = new StudentStudyPeriod(
        null,
        TEST_STUDENT_ID,
        StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2000, 12, 31)
    );

    Response response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(studentStudyPeriod)
        .post("/students/students/{STUDENTID}/studyPeriods", TEST_STUDENT_ID);

    assertOk(role, response, studentPermissions, StudentPermissions.CREATE_STUDENTSTUDYPERIOD);
    
    if (response.getStatusCode() == 200) {
      long id = response.body().jsonPath().getLong("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudents(Role role) throws NoSuchFieldException {
    StudentStudyPeriod studentStudyPeriod = new StudentStudyPeriod(
        null,
        TEST_STUDENT_ID,
        StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2000, 12, 31)
    );

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(studentStudyPeriod)
        .post("/students/students/{STUDENTID}/studyPeriods", TEST_STUDENT_ID);
    
    assertEquals(200, response.getStatusCode());

    long id = response.body().jsonPath().getLong("id");
    try {
      response = given().headers(getAuthHeaders(role))
        .get("/students/students/{STUDENTID}/studyPeriods", TEST_STUDENT_ID);
  
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTSTUDYPERIOD);
    } finally {    
      response = given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentStudyPeriod(Role role) throws NoSuchFieldException {
    StudentStudyPeriod studentStudyPeriod = new StudentStudyPeriod(
        null,
        TEST_STUDENT_ID,
        StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2000, 12, 31)
    );

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(studentStudyPeriod)
        .post("/students/students/{STUDENTID}/studyPeriods", TEST_STUDENT_ID);
    
    assertEquals(200, response.getStatusCode());

    long id = response.body().jsonPath().getLong("id");
    try {
      response = given().headers(getAuthHeaders(role))
        .get("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);
  
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENTSTUDYPERIOD);
    } finally {    
      response = given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudentStudyPeriod(Role role) throws NoSuchFieldException {
    StudentStudyPeriod studentStudyPeriod = new StudentStudyPeriod(
        null,
        TEST_STUDENT_ID,
        StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2000, 12, 31)
    );

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(studentStudyPeriod)
        .post("/students/students/{STUDENTID}/studyPeriods", TEST_STUDENT_ID);

    assertEquals(200, response.statusCode());

    long id = response.body().jsonPath().getLong("id");

    StudentStudyPeriod updatedStudentStudyPeriod = new StudentStudyPeriod(
        id,
        TEST_STUDENT_ID,
        StudentStudyPeriodType.PROLONGED_STUDYENDDATE,
        LocalDate.of(2001, 1, 1),
        null
    );

    try {
      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updatedStudentStudyPeriod)
        .put("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);
  
      assertOk(role, response, studentPermissions, StudentPermissions.UPDATE_STUDENTSTUDYPERIOD);
    } finally {
      response = given().headers(getAdminAuthHeaders())
          .delete("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudentStudyPeriod(Role role) throws NoSuchFieldException {
    StudentStudyPeriod studentStudyPeriod = new StudentStudyPeriod(
        null,
        TEST_STUDENT_ID,
        StudentStudyPeriodType.TEMPORARILY_SUSPENDED,
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2000, 12, 31)
    );

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(studentStudyPeriod)
        .post("/students/students/{STUDENTID}/studyPeriods", TEST_STUDENT_ID);
    
    assertEquals(200, response.getStatusCode());

    long id = response.body().jsonPath().getLong("id");
    response = given().headers(getAuthHeaders(role))
      .delete("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);

    assertOk(role, response, studentPermissions, StudentPermissions.DELETE_STUDENTSTUDYPERIOD, 204);
    
    if (response.statusCode() != 204) {
      response = given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/studyPeriods/{PERIODID}", TEST_STUDENT_ID, id);
    }
  }
    
}
