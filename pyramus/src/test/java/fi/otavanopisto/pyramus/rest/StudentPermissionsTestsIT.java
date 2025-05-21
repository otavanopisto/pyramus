package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Student;
import io.restassured.response.Response;

/**
 * Tests of permissions used in StudentRESTService
 */
public class StudentPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private StudentPermissions studentPermissions = new StudentPermissions();
  private final static long TEST_STUDENT_ID = 3l;
  private static final long SECONDARY_TEST_STUDENT_ID = 13L;
//  private int studentCount = -1;
//  
//  @Before
//  public void beforeTests() throws ClassNotFoundException, SQLException {
//    studentCount = getEntityCount("Student");
//  }
//  
//  @After
//  public void afterTests() throws ClassNotFoundException, SQLException {
//    assertEquals("Student != 0 in test " + testName.getMethodName(), studentCount, getEntityCount("Student"));
//    assertEquals("__UserTags != 0 in test " + testName.getMethodName(), 0, getEntityCount("__UserTags"));
//  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateStudent(Role role) throws NoSuchFieldException {
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
      1l, // studyProgrammeId
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
      Boolean.FALSE, //archived
      null // matriculation eligibility
    );

    Response response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(student)
        .post("/students/students");

    assertOk(role, response, studentPermissions, StudentPermissions.CREATE_STUDENT);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudents(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
        .get("/students/students");

    assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTS);
    
    if (response.statusCode() == 200) {
      if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
        // For group restricted roles there should be only one result student
        response.then().body("id.size()", is(1));
      } else {
        response.then().body("id.size()", is(4));
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudentsByEmail(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
        .get("/students/students?email=student1@bogusmail.com");

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTS, 204);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTS);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudent(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
        .get("/students/students/{ID}", TEST_STUDENT_ID);

    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // Accessible students restricted to groups of the logged user
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENT, 403);
    } else {
      assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENT);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStudentGuider(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
        .get("/students/students/{ID}", SECONDARY_TEST_STUDENT_ID);

    // This should be ok for all roles as the group restricted study guider can
    // also access this user via studentgroup 2.
    assertOk(role, response, studentPermissions, StudentPermissions.FIND_STUDENT);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudent(Role role) throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = new Student(null, 
      1l, 
      "not updated firstName", // firstName
      "not updated lastName", // lastName
      "not updated nickname", // nickname
      "not updated additional", // additionalInfo 
      "not updated additional contact info", // additionalContactInfo
      1l, // nationalityId 
      1l, //languageId
      1l, //municipalityId
      1l, // schoolId
      1l, // activityTypeId
      1l, // examinationTypeId
      1l, // educationalLevelId
      getDate(2020, 11, 2), // studyTimeEnd
      1l, // studyProgrammeId
      null, // curriculumId
      2d, // previousStudies
      "not updated education", // education
      Boolean.FALSE, // lodging
      getDate(2010, 2, 3), // studyStartDate
      getDate(2013, 1, 2), // studyEndDate
      1l, // studyEndReasonId, 
      "not updated studyEndText", // studyEndText, 
      variables, // variables
      Arrays.asList("tag1", "tag2"),  // tags, 
      Boolean.FALSE, //archived
      null // matriculation eligibility
    );
      
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    Long id = response.body().jsonPath().getLong("id");
    try {
      Map<String, String> updateVariables = new HashMap<>();
      updateVariables.put("TV2", "abc");
      updateVariables.put("TV3", "edf");
      
      Student updateStudent = new Student(id, 
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
        1l, // studyProgrammeId
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
        Boolean.FALSE, //archived
        null // matriculation eligibility
      );
      
      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudent)
        .put("/students/students/{ID}", id);

      assertOk(role, response, studentPermissions, StudentPermissions.UPDATE_STUDENT);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateStudentOwner(Role role) throws NoSuchFieldException {
    if (Role.STUDENT == role) {
      Long studentId = getUserIdForRole(Role.STUDENT);

      Response response = given().headers(getAdminAuthHeaders())
          .get("/students/students/{ID}", studentId);
      
      Student oldData = response.as(Student.class);
      
      Long personId = oldData.getPersonId(); // new Long(response.body().jsonPath().getInt("personId"));

      Map<String, String> updateVariables = new HashMap<>();
      updateVariables.put("TV2", "abc");
      updateVariables.put("TV3", "edf");
      
      Student updateStudent = new Student(studentId, 
        personId, 
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
        1l, // studyProgrammeId
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
        Boolean.FALSE, //archived
        null // matriculation eligibility
      );
      
      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudent)
        .put("/students/students/{ID}", studentId);
      
      response.then().assertThat().statusCode(200);
      
      if (response.statusCode() == 200) {
        response = given().headers(getAdminAuthHeaders())
            .contentType("application/json")
            .body(oldData)
            .put("/students/students/{ID}", studentId);
      }
    }
  }  
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteStudent(Role role) throws NoSuchFieldException {
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = new Student(null, 
      1l, 
      "to be deleted", // firstName
      "to be deleted", // lastName
      "to be deleted", // nickname
      "to be deleted", // additionalInfo,
      "to be deleted", // additionalContactInfo
      1l, // nationalityId 
      1l, //languageId
      1l, //municipalityId
      1l, // schoolId
      1l, // activityTypeId
      1l, // examinationTypeId
      1l, // educationalLevelId
      getDate(2020, 11, 2), // studyTimeEnd
      1l, // studyProgrammeId
      null, // curriculumId
      2d, // previousStudies
      "to be deleted", // education
      Boolean.FALSE, // lodging
      getDate(2010, 2, 3), // studyStartDate
      getDate(2013, 1, 2), // studyEndDate
      1l, // studyEndReasonId, 
      "to be deleted", // studyEndText, 
      variables, // variables
      Arrays.asList("tag1", "tag2"),  // tags, 
      Boolean.FALSE, //archived
      null // matriculation eligibility
    );
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    Long id = response.body().jsonPath().getLong("id");
    
    response = given().headers(getAuthHeaders(role))
      .delete("/students/students/{ID}", id);
    
    assertOk(role, response, studentPermissions, StudentPermissions.DELETE_STUDENT, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/students/{ID}?permanent=true", id);
  }
    
}
