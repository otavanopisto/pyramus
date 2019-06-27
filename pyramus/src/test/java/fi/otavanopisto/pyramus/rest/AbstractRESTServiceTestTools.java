package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fi.otavanopisto.pyramus.rest.model.Course;
import fi.otavanopisto.pyramus.rest.model.CourseAssessment;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;
import fi.otavanopisto.pyramus.rest.model.CourseStudent;
import fi.otavanopisto.pyramus.rest.model.Organization;
import fi.otavanopisto.pyramus.rest.model.Person;
import fi.otavanopisto.pyramus.rest.model.Sex;
import fi.otavanopisto.pyramus.rest.model.Student;
import fi.otavanopisto.pyramus.rest.model.StudyProgramme;
import io.restassured.response.Response;

public class AbstractRESTServiceTestTools {

  public AbstractRESTServiceTestTools(AbstractRESTPermissionsTest testClass) {
    this.testClass = testClass;
  }

  public Person createPerson() {
    Person person = new Person(null, null, null, Sex.FEMALE, false, "AbstractRESTServiceTestTools.createPerson()", null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");
    
    response.then().statusCode(200);

    return response.body().as(Person.class);
  }
  
  public void deletePerson(Person person) {
    given().headers(getAdminAuthHeaders())
      .delete("/persons/persons/{ID}", person.getId());
  }
  
  public Student createStudent(Long personId, Long studyProgrammeId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");

    Student student = new Student(
        null, // id 
        personId, // personId 
        "firstName", // firstName
        "lastName", // lastName
        "nickname", // nickname
        "additionalInfo", // additionalInfo 
        "additionalContactInfo", // additionalContactInfo
        1l, // nationalityId 
        1l, //languageId
        1l, //municipalityId
        1l, // schoolId
        1l, // activityTypeId
        1l, // examinationTypeId
        1l, // educationalLevelId
        getDate(2020, 11, 2), // studyTimeEnd
        studyProgrammeId, // studyProgrammeId
        null, // curriculumId
        2d, // previousStudies
        "not updated education", // education
        Boolean.FALSE, // lodging
        getDate(2010, 2, 3), // studyStartDate
        getDate(2013, 1, 2), // studyEndDate
        1l, // studyEndReasonId, 
        "studyEndText", // studyEndText, 
        variables, // variables
        Arrays.asList("tag1", "tag2"),  // tags, 
        Boolean.FALSE //archived
    );
        
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(student)
      .post("/students/students");

    response.then().statusCode(200);

    return response.body().as(Student.class);
  }
  
  public void deleteStudent(Student student) {
    given().headers(getAdminAuthHeaders())
      .delete("/students/students/{ID}?permanent=true", student.getId());
  }
  
  public Course createCourse(String name, Long organizationId) {
    Course course = new Course();
    course.setName(name);
    course.setOrganizationId(organizationId);
    course.setModuleId(1l);
    course.setStateId(1l);
    course.setLength(30d);
    course.setLengthUnitId(1l);
    
    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(course)
        .post("/courses/courses/");

    response.then().statusCode(200);

    return response.body().as(Course.class);
  }
  
  public void deleteCourse(Course course) {
    given().headers(getAdminAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", course.getId())
      .then()
      .statusCode(204);
  }

  public CourseStaffMember createCourseStaffMember(Long courseId, Long staffMemberId, Long roleId) {
    CourseStaffMember courseStaffMember = new CourseStaffMember(null, courseId, staffMemberId, roleId);      

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseStaffMember)
      .post("/courses/courses/{COURSEID}/staffMembers/", courseId);

    assertEquals("Failed to create courseStaffMember", 200, response.statusCode());
    
    return response.as(CourseStaffMember.class);
  }

  public void deleteCourseStaffMember(Long courseId, CourseStaffMember courseStaffMember) {
    given().headers(getAdminAuthHeaders())
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
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseStudent)
      .post("/courses/courses/{COURSEID}/students", courseId);
    
    response.then().statusCode(200);
    
    return response.as(CourseStudent.class);
  }

  public void deleteCourseStudent(CourseStudent courseStudent) {
    given().headers(getAdminAuthHeaders())
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
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", studentId, courseId);
    
    response.then().statusCode(200);
    
    return response.as(CourseAssessment.class);
  }

  public void deleteCourseAssessment(Long courseId, Long studentId, CourseAssessment testASSESSMENT) {
    given().headers(getAdminAuthHeaders())
      .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}?permanent=true", studentId, courseId, testASSESSMENT.getId())
      .then()
      .statusCode(204);
  }

  public Organization createOrganization(String simpleName) {
    Organization organization = new Organization(null, getClass().getSimpleName(), false);
    
    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(organization)
        .post("/organizations");
    
    response.then().statusCode(200);
    return response.as(Organization.class);
  }

  public void deleteOrganization(Organization organization) {
    given().headers(getAdminAuthHeaders())
      .delete("/organizations/{ID}?permanent=true", organization.getId())
      .then()
      .statusCode(204);
  }

  public StudyProgramme createStudyProgramme(Long organizationId, String code, String name, Long categoryId) {
    StudyProgramme studyProgramme = new StudyProgramme(null, organizationId, code, name, categoryId, false);
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    response.then().statusCode(200);
    return response.as(StudyProgramme.class);
  }

  public void deleteStudyProgramme(StudyProgramme studyProgramme) {
    given().headers(getAdminAuthHeaders())
      .delete("/students/studyProgrammes/{ID}?permanent=true", studyProgramme.getId());
  }

  private Map<String, String> getAdminAuthHeaders() {
    return testClass.getAdminAuthHeaders();
  }
  
  protected OffsetDateTime getDate(int year, int monthOfYear, int dayOfMonth) {
    return testClass.getDate(year, monthOfYear, dayOfMonth);
  }

}
