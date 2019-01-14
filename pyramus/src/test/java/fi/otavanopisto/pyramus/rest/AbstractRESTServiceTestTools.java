package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.time.OffsetDateTime;

import fi.otavanopisto.pyramus.rest.model.Course;
import fi.otavanopisto.pyramus.rest.model.CourseAssessment;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;
import fi.otavanopisto.pyramus.rest.model.CourseStudent;
import io.restassured.response.Response;

public class AbstractRESTServiceTestTools {

  public AbstractRESTServiceTestTools(AbstractRESTPermissionsTest testClass) {
    this.testClass = testClass;
  }
  
  public Course createCourse(String name, Long organizationId) {
    Course course = new Course();
    course.setName(name);
    course.setOrganizationId(organizationId);
    course.setModuleId(1l);
    course.setStateId(1l);
    course.setLength(30d);
    course.setLengthUnitId(1l);
    
    Response response = given().headers(testClass.getAdminAuthHeaders())
        .contentType("application/json")
        .body(course)
        .post("/courses/courses/");

    response.then().statusCode(200);

    return response.body().as(Course.class);
  }
  
  public void deleteCourse(Course course) {
    given().headers(testClass.getAdminAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", course.getId())
      .then()
      .statusCode(204);
  }

  public CourseStaffMember createCourseStaffMember(Long courseId, Long staffMemberId, Long roleId) {
    CourseStaffMember courseStaffMember = new CourseStaffMember(null, courseId, staffMemberId, roleId);      

    Response response = given().headers(testClass.getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseStaffMember)
      .post("/courses/courses/{COURSEID}/staffMembers/", courseId);

    assertEquals("Failed to create courseStaffMember", 200, response.statusCode());
    
    return response.as(CourseStaffMember.class);
  }

  public void deleteCourseStaffMember(Long courseId, CourseStaffMember courseStaffMember) {
    given().headers(testClass.getAdminAuthHeaders())
      .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", courseId, courseStaffMember.getId())
      .then()
      .statusCode(204);
  }

  public CourseStudent createCourseStudent(Long courseId, Long studentId) {
    CourseStudent courseStudent = new CourseStudent();
    courseStudent.setCourseId(courseId);
    courseStudent.setStudentId(studentId);
    courseStudent.setLodging(Boolean.FALSE);
    courseStudent.setEnrolmentTime(OffsetDateTime.now());
    courseStudent.setEnrolmentTypeId(1l);
    courseStudent.setParticipationTypeId(1l);
    
    Response response = given().headers(testClass.getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseStudent)
      .post("/courses/courses/{COURSEID}/students", courseId);
    
    response.then().statusCode(200);
    
    return response.as(CourseStudent.class);
  }

  public void deleteCourseStudent(CourseStudent courseStudent) {
    given().headers(testClass.getAdminAuthHeaders())
      .delete("/courses/courses/{COURSEID}/students/{ID}?permanent=true", courseStudent.getCourseId(), courseStudent.getId())
      .then()
      .statusCode(204);
  }

  private AbstractRESTPermissionsTest testClass;

  public CourseAssessment createCourseAssessment(Long courseId, Long studentId, Long courseStudentId) {
    CourseAssessment courseAssessment = new CourseAssessment();
    courseAssessment.setCourseStudentId(courseStudentId);
    courseAssessment.setGradeId(2l);
    courseAssessment.setGradingScaleId(1l);
    courseAssessment.setAssessorId(1l);
    courseAssessment.setDate(OffsetDateTime.now());
    courseAssessment.setPassing(Boolean.TRUE);
    
    Response response = given().headers(testClass.getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", studentId, courseId);
    
    response.then().statusCode(200);
    
    return response.as(CourseAssessment.class);
  }

  public void deleteCourseAssessment(Long courseId, Long studentId, CourseAssessment testASSESSMENT) {
    given().headers(testClass.getAdminAuthHeaders())
      .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}?permanent=true", studentId, courseId, testASSESSMENT.getId())
      .then()
      .statusCode(204);
  }

}
