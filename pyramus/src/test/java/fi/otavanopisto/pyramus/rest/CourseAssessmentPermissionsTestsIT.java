package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CourseAssessmentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.CourseAssessment;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;
import io.restassured.response.Response;

@RunWith(Parameterized.class)
public class CourseAssessmentPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private static final long TEST_COURSESTUDENTID = 5;
  private static final long TEST_GRADEID = 2;
  private static final long TEST_ASSESSORID = 6;
  private static final long TEST_STUDENTID = 3;
  private static final long TEST_COURSEID = 1000;
  private static final long TEST_COURSETEACHER_ROLEID = 1;

  // STUDYGUIDER_ prefixed id's are for student who is in a group lead by studyguider test role
  private static final long STUDYGUIDER_TEST_STUDENTID = 13L;
  private static final long STUDYGUIDER_TEST_COURSESTUDENTID = 7L;
  private static final long STUDYGUIDER_TEST_COURSEID = 1001;

  private CourseAssessmentPermissions assessmentPermissions = new CourseAssessmentPermissions();
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseAssessmentPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testCreateCourseAssessment() throws NoSuchFieldException {
    CourseAssessment courseAssessment = new CourseAssessment(null, TEST_COURSESTUDENTID, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Test assessment for test student on test course.", Boolean.TRUE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, TEST_COURSEID);
    
    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.CREATE_COURSEASSESSMENT, 403);
    } else {
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.CREATE_COURSEASSESSMENT);
    }

    if (response.statusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", TEST_STUDENTID, TEST_COURSEID, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testCreateCourseAssessmentAsStudyGuider() throws NoSuchFieldException {
    CourseAssessment courseAssessment = new CourseAssessment(null, STUDYGUIDER_TEST_COURSESTUDENTID, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Test assessment for test student on test course.", Boolean.TRUE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID);

    // Expected to be ok for all roles because study guider has access to this test student also
    assertOk(response, assessmentPermissions, CourseAssessmentPermissions.CREATE_COURSEASSESSMENT);

    if (response.statusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID, id)
        .then()
        .statusCode(204);
    }
  }
  
  /**
   * Tests that the role can still create an assessment if they are on a course as a staffmember.
   */
  @Test
  public void testCreateCourseAssessmentAsCourseTeacher() throws NoSuchFieldException {
    if (StringUtils.equals(Role.TEACHER.toString(), getRole()) || StringUtils.equals(Role.STUDY_GUIDER.toString(), getRole())) {
      CourseStaffMember tempCourseTeacher = new CourseStaffMember(null, TEST_COURSEID, getUserIdForRole(getRole()), TEST_COURSETEACHER_ROLEID);      

      Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(tempCourseTeacher)
        .post("/courses/courses/{COURSEID}/staffMembers/", TEST_COURSEID);

      assertEquals("Failed to create temporary courseStaffMember", 200, response.statusCode());
      
      if (response.statusCode() == 200) {
        int tempCourseTeacherId = response.body().jsonPath().getInt("id");
        
        try {
          CourseAssessment courseAssessment = new CourseAssessment(null, TEST_COURSESTUDENTID, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Test assessment for test student on test course.", Boolean.TRUE);
          
          response = given().headers(getAuthHeaders())
            .contentType("application/json")
            .body(courseAssessment)
            .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, TEST_COURSEID);

          assertPermission(CourseAssessmentPermissions.FIND_COURSEASSESSMENT, 200, response.statusCode());
    
          if (response.statusCode() == 200) {
            int id = response.body().jsonPath().getInt("id");
            
            given().headers(getAdminAuthHeaders())
              .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", TEST_STUDENTID, TEST_COURSEID, id)
              .then()
              .statusCode(204);
          }
        } finally {
          given().headers(getAdminAuthHeaders())
            .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", TEST_COURSEID, tempCourseTeacherId)
            .then()
            .statusCode(204);
        }
      }
    }
  }
  
  @Test
  public void testFindCourseAssessment() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", TEST_STUDENTID, TEST_COURSEID, 1);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.FIND_COURSEASSESSMENT, 403);
    } else {
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.FIND_COURSEASSESSMENT);
    }
  }

  @Test
  public void testFindCourseAssessmentAsStudyGuider() throws NoSuchFieldException {
    CourseAssessment courseAssessment = new CourseAssessment(null, STUDYGUIDER_TEST_COURSESTUDENTID, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Test assessment for test student on test course.", Boolean.TRUE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID);

    if (response.statusCode() == 200) {
      int courseAssessmentId = response.body().jsonPath().getInt("id");

      try {
        response = given().headers(getAuthHeaders())
          .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID, courseAssessmentId);

        // Study Guider can access this students' assessment
        assertOk(response, assessmentPermissions, CourseAssessmentPermissions.FIND_COURSEASSESSMENT);
      } finally {
        given().headers(getAdminAuthHeaders())
          .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID, courseAssessmentId)
          .then()
          .statusCode(204);
      }
    }
  }

  @Test
  public void testFindCourseAssessmentAsCourseTeacher() throws NoSuchFieldException {
    if (StringUtils.equals(Role.TEACHER.toString(), getRole()) || StringUtils.equals(Role.STUDY_GUIDER.toString(), getRole())) {
      CourseStaffMember tempCourseTeacher = new CourseStaffMember(null, TEST_COURSEID, getUserIdForRole(getRole()), TEST_COURSETEACHER_ROLEID);      

      Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(tempCourseTeacher)
        .post("/courses/courses/{COURSEID}/staffMembers/", TEST_COURSEID);

      assertEquals("Failed to create temporary courseStaffMember", 200, response.statusCode());
      
      if (response.statusCode() == 200) {
        int tempCourseTeacherId = response.body().jsonPath().getInt("id");
        
        try {
          response = given().headers(getAuthHeaders())
            .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", TEST_STUDENTID, TEST_COURSEID, 1);

          assertPermission(CourseAssessmentPermissions.FIND_COURSEASSESSMENT, 200, response.statusCode());
        } finally {
          given().headers(getAdminAuthHeaders())
            .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", TEST_COURSEID, tempCourseTeacherId)
            .then()
            .statusCode(204);
        }
      }
    }
  }

  @Test
  public void listCourseAssessments() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, TEST_COURSEID );

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS, 403);
    } else {
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS);
    }
  }
  
  @Test
  public void listCourseAssessmentsAsStudyGuider() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID );

    assertOk(response, assessmentPermissions, CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS);
  }
  
  @Test
  public void listCourseAssessmentsAsCourseTeacher() throws NoSuchFieldException {
    if (StringUtils.equals(Role.TEACHER.toString(), getRole()) || StringUtils.equals(Role.STUDY_GUIDER.toString(), getRole())) {
      CourseStaffMember tempCourseTeacher = new CourseStaffMember(null, TEST_COURSEID, getUserIdForRole(getRole()), TEST_COURSETEACHER_ROLEID);      

      Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(tempCourseTeacher)
        .post("/courses/courses/{COURSEID}/staffMembers/", TEST_COURSEID);

      assertEquals("Failed to create temporary courseStaffMember", 200, response.statusCode());
      
      if (response.statusCode() == 200) {
        int tempCourseTeacherId = response.body().jsonPath().getInt("id");
        
        try {
          response = given().headers(getAuthHeaders())
            .get("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", TEST_STUDENTID, TEST_COURSEID);
      
          assertOk(response, assessmentPermissions, CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS);
        } finally {
          given().headers(getAdminAuthHeaders())
            .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", TEST_COURSEID, tempCourseTeacherId)
            .then()
            .statusCode(204);
        }
      }
    }
  }
  
  @Test
  public void testCountCourseAssessments() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/courseAssessmentCount", TEST_STUDENTID);
    
    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS, 403);
    } else {
      assertOk(response, assessmentPermissions, CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS);
    }
  }

  @Test
  public void testUpdateCourseAssessment() throws NoSuchFieldException {
    CourseAssessment courseAssessment = new CourseAssessment(null, 6l, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Not Updated.", Boolean.TRUE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", 4, TEST_COURSEID);

    if (response.statusCode() == 200) {
      Long id = new Long(response.body().jsonPath().getInt("id"));
      try {
        CourseAssessment updatedCourseAssessment = new CourseAssessment(id, 6l, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 2, 1), "Updated", Boolean.TRUE);
  
        response = given().headers(getAuthHeaders())
          .contentType("application/json")
          .body(updatedCourseAssessment)
          .put("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, id);
  
        if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
          // Accessible students restricted to groups of the logged user
          assertOk(response, assessmentPermissions, CourseAssessmentPermissions.UPDATE_COURSEASSESSMENT, 403);
        } else {
          assertOk(response, assessmentPermissions, CourseAssessmentPermissions.UPDATE_COURSEASSESSMENT);
        }
      } finally {
        given().headers(getAdminAuthHeaders())
          .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, id)
          .then()
          .statusCode(204);
      }
    }
  }

  @Test
  public void testUpdateCourseAssessmentStudyGuider() throws NoSuchFieldException {
    CourseAssessment courseAssessment = new CourseAssessment(null, STUDYGUIDER_TEST_COURSESTUDENTID, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Not Updated.", Boolean.TRUE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(courseAssessment)
      .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID);

    if (response.statusCode() == 200) {
      Long id = new Long(response.body().jsonPath().getInt("id"));
      try {
        CourseAssessment updatedCourseAssessment = new CourseAssessment(id, STUDYGUIDER_TEST_COURSESTUDENTID, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 2, 1), "Updated", Boolean.TRUE);
  
        response = given().headers(getAuthHeaders())
          .contentType("application/json")
          .body(updatedCourseAssessment)
          .put("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID, id);
  
        assertOk(response, assessmentPermissions, CourseAssessmentPermissions.UPDATE_COURSEASSESSMENT);
      } finally {
        given().headers(getAdminAuthHeaders())
          .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}", STUDYGUIDER_TEST_STUDENTID, STUDYGUIDER_TEST_COURSEID, id)
          .then()
          .statusCode(204);
      }
    }
  }
  
  @Test
  public void testUpdateCourseAssessmentAsCourseTeacher() throws NoSuchFieldException {
    if (StringUtils.equals(Role.TEACHER.toString(), getRole()) || StringUtils.equals(Role.STUDY_GUIDER.toString(), getRole())) {
      CourseStaffMember tempCourseTeacher = new CourseStaffMember(null, TEST_COURSEID, getUserIdForRole(getRole()), TEST_COURSETEACHER_ROLEID);      

      Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json")
        .body(tempCourseTeacher)
        .post("/courses/courses/{COURSEID}/staffMembers/", TEST_COURSEID);

      assertEquals("Failed to create temporary courseStaffMember", 200, response.statusCode());
      
      if (response.statusCode() == 200) {
        int tempCourseTeacherId = response.body().jsonPath().getInt("id");
        
        try {
          CourseAssessment courseAssessment = new CourseAssessment(null, 6l, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 1, 1), "Not Updated.", Boolean.TRUE);
          
          response = given().headers(getAdminAuthHeaders())
            .contentType("application/json")
            .body(courseAssessment)
            .post("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/", 4, TEST_COURSEID);
      
          if (response.statusCode() == 200) {
            Long id = new Long(response.body().jsonPath().getInt("id"));
            try {
              CourseAssessment updatedCourseAssessment = new CourseAssessment(id, 6l, TEST_GRADEID, 1l, TEST_ASSESSORID, getDate(2015, 2, 1), "Updated", Boolean.TRUE);
        
              response = given().headers(getAuthHeaders())
                .contentType("application/json")
                .body(updatedCourseAssessment)
                .put("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, id);
        
              assertPermission(CourseAssessmentPermissions.FIND_COURSEASSESSMENT, 200, response.statusCode());
            } finally {
              given().headers(getAdminAuthHeaders())
                .delete("/students/students/{STUDENTID}/courses/{COURSEID}/assessments/{ID}",4, TEST_COURSEID, id)
                .then()
                .statusCode(204);
            }
          }
        } finally {
          given().headers(getAdminAuthHeaders())
            .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", TEST_COURSEID, tempCourseTeacherId)
            .then()
            .statusCode(204);
        }
      }
    }
  }
  
}
