package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Organization;
import fi.otavanopisto.pyramus.rest.model.Student;
import fi.otavanopisto.pyramus.rest.model.StudyProgramme;
import fi.otavanopisto.pyramus.security.impl.permissions.OrganizationPermissions;


/**
 * Tests of permissions used in StudentRESTService
 */
@RunWith(Parameterized.class)
public class StudentCrossOrganizationPermissionsTestsIT extends AbstractRESTPermissionsTest {

  public StudentCrossOrganizationPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  private StudentPermissions studentPermissions = new StudentPermissions();
  private OrganizationPermissions organizationPermissions = new OrganizationPermissions();
//  private final static long TEST_STUDENT_ID = 3l;
//  private static final long SECONDARY_TEST_STUDENT_ID = 13L;

  private Organization organization;
  private StudyProgramme studyProgramme;
  
  @Before
  public void beforeTests() {
    organization = tools().createOrganization(getClass().getSimpleName());
    studyProgramme = tools().createStudyProgramme(organization.getId(), "TEST", getClass().getSimpleName(), 1l);
  }
  
  @After
  public void after() {
    tools().deleteStudyProgramme(studyProgramme);
    tools().deleteOrganization(organization);
  }
  
  @Test
  public void testCreateStudent() throws NoSuchFieldException {
    /**
     * Tests permission to create student if the creator is not in the
     * same organization.
     */
    
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = new Student(null, 
      1l, // personId
      "to be", // firstName
      "created", // lastName
      "cretest", // nickname
      "additional", // additionalInfo
      "additional contact info", // additionalContactInfo
      1l, // nationalityId 
      1l, //languageId
      1l, //municipalityId
      1l, // schoolId
      1l, // activityTypeId
      1l, // examinationTypeId
      1l, // educationalLevelId
      getDate(2020, 11, 2), // studyTimeEnd
      studyProgramme.getId(), // studyProgrammeId
      null, // curriculumId
      2d, // previousStudies
      "Carpenter", // education
      Boolean.FALSE, // lodging
      getDate(2010, 2, 3), // studyStartDate
      getDate(2013, 1, 2), // studyEndDate
      1l, // studyEndReasonId, 
      "Testing...", // studyEndText, 
      variables, // variables
      Arrays.asList("tag1", "tag2"),  // tags, 
      Boolean.FALSE //archived
    );

    Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(student)
        .post("/students/students");

    int expectedStatusCode = roleIsAllowed(role, studentPermissions, StudentPermissions.CREATE_STUDENT) ? 200 : 403;
    assertOk(response, organizationPermissions, OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS, expectedStatusCode);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{ID}?permanent=true", id);
    }
  }
  
//  @Test
//  public void testListStudents() throws NoSuchFieldException {
//    Response response = given().headers(getAuthHeaders())
//        .get("/students/students");
//
//    assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTS);
//    
//    if (response.statusCode() == 200) {
//      if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
//        // For group restricted roles there should be only one result student
//        response.then().body("id.size()", is(1));
//      } else {
//        response.then().body("id.size()", is(4));
//      }
//    }
//  }
//
//  @Test
//  public void testListStudentsByEmail() throws NoSuchFieldException {
//    Response response = given().headers(getAuthHeaders())
//        .get("/students/students?email=student1@bogusmail.com");
//
//    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
//      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTS, 204);
//    } else {
//      assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTS);
//    }
//  }
  
  @Test
  public void testFindStudent() throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = tools().createStudent(1L, studyProgramme.getId());
    try {
      Response response = given().headers(getAuthHeaders())
          .get("/students/students/{ID}", student.getId());
  
      int expectedStatusCode = roleIsAllowed(role, studentPermissions, StudentPermissions.FIND_STUDENT) ? 200 : 403;
      assertOk(response, organizationPermissions, OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS, expectedStatusCode);
    } finally {
      tools().deleteStudent(student);
    }
  }
  
//  @Test
//  public void testFindStudentGuider() throws NoSuchFieldException {
//    Response response = given().headers(getAuthHeaders())
//        .get("/students/students/{ID}", SECONDARY_TEST_STUDENT_ID);
//
//    // This should be ok for all roles as the group restricted study guider can
//    // also access this user via studentgroup 2.
//    assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENT);
//  }
  
  @Test
  public void testUpdateStudent() throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = tools().createStudent(1L, studyProgramme.getId());
    try {
      Map<String, String> updateVariables = new HashMap<>();
      updateVariables.put("TV2", "abc");
      updateVariables.put("TV3", "edf");
      
      Student updateStudent = new Student(student.getId(), 
        2l, 
        "updated firstName", // firstName
        "updated lastName", // lastName
        "updated nickname", // nickname
        "updated additional", // additionalInfo 
        "updated additional contact info", // additionalInfo 
        2l, // nationalityId 
        2l, //languageId
        2l, //municipalityId
        2l, // schoolId
        2l, // activityTypeId
        2l, // examinationTypeId
        2l, // educationalLevelId
        getDate(2030, 11, 2), // studyTimeEnd
        studyProgramme.getId(), // studyProgrammeId
        null, // curriculumId
        2d, // previousStudies
        "updated education", // education
        Boolean.TRUE, // lodging
        getDate(2020, 2, 3), // studyStartDate
        getDate(2033, 1, 2), // studyEndDate
        2l, // studyEndReasonId, 
        "updated studyEndText", // studyEndText, 
        updateVariables, // variables
        Arrays.asList("tag2", "tag3"),  // tags, 
        Boolean.FALSE //archived
      );
      
      Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudent)
        .put("/students/students/{ID}", student.getId());

      int expectedStatusCode = roleIsAllowed(role, studentPermissions, StudentPermissions.UPDATE_STUDENT) ? 200 : 403;
      assertOk(response, organizationPermissions, OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS, expectedStatusCode);
    } finally {
      tools().deleteStudent(student);
    }
  }
  
//  @Test
//  public void testUpdateStudentOwner() throws NoSuchFieldException {
//    if (Role.STUDENT.name().equals(this.role)) {
//      Long studentId = getUserIdForRole(Role.STUDENT.name());
//
//      Response response = given().headers(getAdminAuthHeaders())
//          .get("/students/students/{ID}", studentId);
//      
//      Student oldData = response.as(Student.class);
//      
//      Long personId = oldData.getPersonId(); // new Long(response.body().jsonPath().getInt("personId"));
//
//      Map<String, String> updateVariables = new HashMap<>();
//      updateVariables.put("TV2", "abc");
//      updateVariables.put("TV3", "edf");
//      
//      Student updateStudent = new Student(studentId, 
//        personId, 
//        "updated firstName", // firstName
//        "updated lastName", // lastName
//        "updated nickname", // nickname
//        "updated additional", // additionalInfo 
//        "updated additional contact info", // additionalInfo 
//        2l, // nationalityId 
//        2l, //languageId
//        2l, //municipalityId
//        2l, // schoolId
//        2l, // activityTypeId
//        2l, // examinationTypeId
//        2l, // educationalLevelId
//        getDate(2030, 11, 2), // studyTimeEnd
//        1l, // studyProgrammeId
//        null, // curriculumId
//        2d, // previousStudies
//        "updated education", // education
//        Boolean.TRUE, // lodging
//        getDate(2020, 2, 3), // studyStartDate
//        getDate(2033, 1, 2), // studyEndDate
//        2l, // studyEndReasonId, 
//        "updated studyEndText", // studyEndText, 
//        updateVariables, // variables
//        Arrays.asList("tag2", "tag3"),  // tags, 
//        Boolean.FALSE //archived
//      );
//      
//      response = given().headers(getAuthHeaders())
//        .contentType("application/json")
//        .body(updateStudent)
//        .put("/students/students/{ID}", studentId);
//      
//      response.then().assertThat().statusCode(200);
//      
//      if (response.statusCode() == 200) {
//        response = given().headers(getAdminAuthHeaders())
//            .contentType("application/json")
//            .body(oldData)
//            .put("/students/students/{ID}", studentId);
//      }
//    }
//  }  
  
  @Test
  public void testDeleteStudent() throws NoSuchFieldException {
    Student student = tools().createStudent(1L, studyProgramme.getId());

    try {
      Response response = given().headers(getAuthHeaders())
        .delete("/students/students/{ID}", student.getId());
  
      // Both DELETE_STUDENT and ACCESS_ALL_ORGANIZATIONS are  needed
      int expectedStatusCode = roleIsAllowed(role, studentPermissions, StudentPermissions.DELETE_STUDENT) ? 204 : 403;
      assertOk(response, organizationPermissions, OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS, expectedStatusCode);
    } finally {
      tools().deleteStudent(student);
    }
  }
    
}
