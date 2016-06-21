package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Student;


/**
 * Tests of permissions used in StudentRESTService
 */
@RunWith(Parameterized.class)
public class StudentPermissionsTestsIT extends AbstractRESTPermissionsTest {

  public StudentPermissionsTestsIT(String role) {
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
  private final static long TEST_STUDENT_ID = 3l;
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
  
  @Test
  public void testCreateStudent() throws NoSuchFieldException {
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
      Boolean.FALSE //archived
    );

    Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(student)
        .post("/students/students");

    assertOk(response, studentPermissions, StudentPermissions.CREATE_STUDENT);
    
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testListStudents() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
        .get("/students/students");

    assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTS);
  }

  @Test
  public void testListStudentsByEmail() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
        .get("/students/students?email=student1@bogusmail.com");

    assertOk(response, studentPermissions, StudentPermissions.LIST_STUDENTSBYEMAIL);
  }
  
  @Test
  public void testFindStudent() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
        .get("/students/students/{ID}", TEST_STUDENT_ID);

    assertOk(response, studentPermissions, StudentPermissions.FIND_STUDENT);
  }
  
  @Test
  public void testUpdateStudent() throws NoSuchFieldException {
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
      Boolean.FALSE //archived
    );
      
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
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
        Boolean.FALSE //archived
      );
      
      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudent)
        .put("/students/students/{ID}", id);

      assertOk(response, studentPermissions, StudentPermissions.UPDATE_STUDENT);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{ID}?permanent=true", id);
    }
  }

//  TODO: this breaks all other tests
//  @Test
//  public void testUpdateStudentOwner() throws NoSuchFieldException {
//    if (Role.STUDENT.name().equals(this.role)) {
//      Long studentId = getUserIdForRole(Role.STUDENT.name());
//
//      Response response = given().headers(getAdminAuthHeaders())
//          .get("/students/students/{ID}", studentId);
//      
//      Long personId = new Long(response.body().jsonPath().getInt("personId"));
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
//    }
//  }  
  
  @Test
  public void testDeleteStudent() throws NoSuchFieldException {
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
      Boolean.FALSE //archived
    );
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/students/{ID}", id);
    
    assertOk(response, studentPermissions, StudentPermissions.DELETE_STUDENT, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/students/{ID}?permanent=true", id);
  }
    
}
