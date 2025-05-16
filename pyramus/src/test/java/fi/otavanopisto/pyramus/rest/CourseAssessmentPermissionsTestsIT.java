package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.EnumSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CourseAssessmentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Course;
import fi.otavanopisto.pyramus.rest.model.CourseAssessment;
import fi.otavanopisto.pyramus.rest.model.CourseModule;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum;
import fi.otavanopisto.pyramus.rest.model.CourseStudent;
import io.restassured.response.Response;

public class CourseAssessmentPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private static final long TEST_GRADEID = 2;
  private static final long TEST_ASSESSORID = 6;
  private static final long TEST_STUDENTID = 3;
  private static final CourseStaffMemberRoleEnum TEST_COURSETEACHER_ROLE = CourseStaffMemberRoleEnum.COURSE_TEACHER;

  private CourseAssessmentPermissions assessmentPermissions = new CourseAssessmentPermissions();
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  private Course testCOURSE = null;
  private CourseStudent testCOURSESTUDENT = null;
  private CourseModule testCOURSEMODULE = null;
  
  @BeforeAll
  public void setup() {
    testCOURSE = tools().createCourse("CourseAssessmentPermissionsTestsIT", 1l);
    testCOURSEMODULE = testCOURSE.getCourseModules().iterator().next();
    testCOURSESTUDENT = tools().createCourseStudent(testCOURSE.getId(), TEST_STUDENTID);
  }
  
  @AfterAll
  public void teardown() {
    tools().deleteCourseStudent(testCOURSESTUDENT);
    tools().deleteCourse(testCOURSE);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateCourseAssessment(Role role) throws NoSuchFieldException {
    CourseAssessment courseAssessment = new CourseAssessment(null, testCOURSESTUDENT.getId(), testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Test assessment for test student on test course.", Boolean.TRUE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, testCOURSE.getId());
    
    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.CREATE_COURSEASSESSMENT, 403);
    } else {
      assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.CREATE_COURSEASSESSMENT);
    }

    if (response.statusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}?permanent=true", TEST_STUDENTID, testCOURSE.getId(), id)
        .then()
        .statusCode(204);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateCourseAssessmentAsStudent(Role role) throws NoSuchFieldException {
    if (role == Role.STUDENT) {
      CourseAssessment courseAssessment = new CourseAssessment(null, testCOURSESTUDENT.getId(), testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Test assessment for test student on test course.", Boolean.TRUE);
      
      Response response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(courseAssessment)
        .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", getUserIdForRole(role), testCOURSE.getId());
      
      assertEquals("Student can create an assessment!", 403, response.statusCode());
      
      if (response.statusCode() == 200) {
        int id = response.body().jsonPath().getInt("id");
        
        given().headers(getAdminAuthHeaders())
          .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}?permanent=true", TEST_STUDENTID, testCOURSE.getId(), id)
          .then()
          .statusCode(204);
      }
    }
  }
  
  /**
   * Tests that the role can still create an assessment if they are on a course as a staffmember.
   */
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateCourseAssessmentAsCourseTeacher(Role role) throws NoSuchFieldException {
    if (EnumSet.of(Role.TEACHER, Role.STUDY_GUIDER).contains(role)) {
      CourseStaffMember tempCourseTeacher = tools().createCourseStaffMember(testCOURSE.getId(), getUserIdForRole(role), TEST_COURSETEACHER_ROLE);

      try {
        CourseAssessment courseAssessment = new CourseAssessment(null, testCOURSESTUDENT.getId(), testCOURSEMODULE.getId(), TEST_GRADEID, 1l, getUserIdForRole(role), getDate(2015, 1, 1), "Test assessment for test student on test course.", Boolean.TRUE);
        
        Response response = given().headers(getAuthHeaders(role))
          .contentType("application/json")
          .body(courseAssessment)
          .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, testCOURSE.getId());

        assertEquals("Course Teacher couldn't create assessment.", 200, response.getStatusCode());
  
        if (response.statusCode() == 200) {
          int id = response.body().jsonPath().getInt("id");
          
          given().headers(getAdminAuthHeaders())
            .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}?permanent=true", TEST_STUDENTID, testCOURSE.getId(), id)
            .then()
            .statusCode(204);
        }
      } finally {
        tools().deleteCourseStaffMember(testCOURSE.getId(), tempCourseTeacher);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindCourseAssessment(Role role) throws NoSuchFieldException {
    CourseAssessment testASSESSMENT = tools().createCourseAssessment(testCOURSE.getId(), testCOURSEMODULE.getId(), TEST_STUDENTID, testCOURSESTUDENT.getId(), 1l);
    try {
      Response response = given().headers(getAuthHeaders(role))
        .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", testCOURSESTUDENT.getStudentId(), testCOURSE.getId(), testASSESSMENT.getId());
  
      if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
        // Accessible students restricted to groups of the logged user
        assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.FIND_COURSEASSESSMENT, 403);
      } else {
        assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.FIND_COURSEASSESSMENT);
      }
    } finally {
      tools().deleteCourseAssessment(testCOURSE.getId(), TEST_STUDENTID, testASSESSMENT);
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindCourseAssessmentAsCourseTeacher(Role role) throws NoSuchFieldException {
    if (EnumSet.of(Role.TEACHER, Role.STUDY_GUIDER).contains(role)) {
      CourseAssessment testASSESSMENT = tools().createCourseAssessment(testCOURSE.getId(), testCOURSEMODULE.getId(), TEST_STUDENTID, testCOURSESTUDENT.getId(), 1l);
      try {
        // Add the current test user to the course so they have access to the course assessments
        CourseStaffMember tempCourseTeacher = tools().createCourseStaffMember(testCOURSE.getId(), getUserIdForRole(role), TEST_COURSETEACHER_ROLE);
          
        try {
          Response response = given().headers(getAuthHeaders(role))
            .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", testCOURSESTUDENT.getStudentId(), testCOURSE.getId(), testASSESSMENT.getId());
  
          assertPermission(role, CourseAssessmentPermissions.FIND_COURSEASSESSMENT, 200, response.statusCode());
        } finally {
          tools().deleteCourseStaffMember(testCOURSE.getId(), tempCourseTeacher);
        }
      } finally {
        tools().deleteCourseAssessment(testCOURSE.getId(), TEST_STUDENTID, testASSESSMENT);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void listCourseAssessments(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, testCOURSE.getId());

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS, 403);
    } else {
      assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void listCourseAssessmentsAsCourseTeacher(Role role) throws NoSuchFieldException {
    if (EnumSet.of(Role.TEACHER, Role.STUDY_GUIDER).contains(role)) {
      CourseStaffMember tempCourseTeacher = tools().createCourseStaffMember(testCOURSE.getId(), getUserIdForRole(role), TEST_COURSETEACHER_ROLE);

      try {
        Response response = given().headers(getAuthHeaders(role))
          .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, testCOURSE.getId());
    
        assertEquals("Course Teacher couldn't list course assessments", 200, response.statusCode());
      } finally {
        given().headers(getAdminAuthHeaders())
          .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", testCOURSE.getId(), tempCourseTeacher.getId())
          .then()
          .statusCode(204);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCountCourseAssessments(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/students/{STUDENTID}/courseAssessmentCount", TEST_STUDENTID);
    
    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS, 403);
    } else {
      assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS);
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateCourseAssessment(Role role) throws NoSuchFieldException {
    CourseAssessment courseAssessment = new CourseAssessment(null, 6l, testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Not Updated.", Boolean.TRUE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", 4, testCOURSE.getId());

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      try {
        CourseAssessment updatedCourseAssessment = new CourseAssessment(id, 6l, testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 2, 1), "Updated", Boolean.TRUE);
  
        response = given().headers(getAuthHeaders(role))
          .contentType("application/json")
          .body(updatedCourseAssessment)
          .put("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, testCOURSE.getId(), id);
  
        if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
          // Accessible students restricted to groups of the logged user
          assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.UPDATE_COURSEASSESSMENT, 403);
        } else {
          assertOk(role, response, assessmentPermissions, CourseAssessmentPermissions.UPDATE_COURSEASSESSMENT);
        }
      } finally {
        given().headers(getAdminAuthHeaders())
          .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, testCOURSE.getId(), id)
          .then()
          .statusCode(204);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateCourseAssessmentAsStudent(Role role) throws NoSuchFieldException {
    if (role == Role.STUDENT) {
      long studentId = getUserIdForRole(Role.STUDENT);
      CourseAssessment courseAssessment = new CourseAssessment(null, 6l, testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Not Updated.", Boolean.TRUE);
      
      Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(courseAssessment)
        .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", studentId, testCOURSE.getId());
  
      if (response.statusCode() == 200) {
        Long id = response.body().jsonPath().getLong("id");
        try {
          CourseAssessment updatedCourseAssessment = new CourseAssessment(id, 6l, testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 2, 1), "Updated", Boolean.TRUE);
    
          response = given().headers(getAuthHeaders(role))
            .contentType("application/json")
            .body(updatedCourseAssessment)
            .put("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", studentId, testCOURSE.getId(), id);
    
          assertEquals("Student can update his/her own assessment!", 403, response.statusCode());
        } finally {
          given().headers(getAdminAuthHeaders())
            .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", studentId, testCOURSE.getId(), id)
            .then()
            .statusCode(204);
        }
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateCourseAssessmentAsCourseTeacher(Role role) throws NoSuchFieldException {
    if (EnumSet.of(Role.TEACHER, Role.STUDY_GUIDER).contains(role)) {
      CourseStaffMember tempCourseTeacher = new CourseStaffMember(null, testCOURSE.getId(), getUserIdForRole(role), TEST_COURSETEACHER_ROLE);      

      Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(tempCourseTeacher)
        .post("/courses/courses/{COURSEID}/staffMembers/", testCOURSE.getId());

      assertEquals("Failed to create temporary courseStaffMember", 200, response.statusCode());
      
      if (response.statusCode() == 200) {
        int tempCourseTeacherId = response.body().jsonPath().getInt("id");
        
        try {
          CourseAssessment courseAssessment = new CourseAssessment(null, 6l, testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Not Updated.", Boolean.TRUE);
          
          response = given().headers(getAdminAuthHeaders())
            .contentType("application/json")
            .body(courseAssessment)
            .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", 4, testCOURSE.getId());
      
          if (response.statusCode() == 200) {
            Long id = response.body().jsonPath().getLong("id");
            try {
              CourseAssessment updatedCourseAssessment = new CourseAssessment(id, 6l, testCOURSEMODULE.getId(), TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 2, 1), "Updated", Boolean.TRUE);
        
              response = given().headers(getAuthHeaders(role))
                .contentType("application/json")
                .body(updatedCourseAssessment)
                .put("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, testCOURSE.getId(), id);
        
              assertPermission(role, CourseAssessmentPermissions.FIND_COURSEASSESSMENT, 200, response.statusCode());
            } finally {
              given().headers(getAdminAuthHeaders())
                .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, testCOURSE.getId(), id)
                .then()
                .statusCode(204);
            }
          }
        } finally {
          given().headers(getAdminAuthHeaders())
            .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", testCOURSE.getId(), tempCourseTeacherId)
            .then()
            .statusCode(204);
        }
      }
    }
  }
  
}
