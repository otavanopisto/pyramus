package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Student;

public class StudentTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudent() {
    Map<String, String> variables = new HashMap<String, String>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = new Student(null, 
      1l, // abstractStudentId
      "to be", // firstName
      "created", // lastName
      "cretest", // nickname
      "additional", // additionalInfo 
      1l, // nationalityId 
      1l, //languageId
      1l, //municipalityId
      1l, // schoolId
      1l, // activityTypeId
      1l, // examinationTypeId
      1l, // educationalLevelId
      getDate(2020, 11, 2), // studyTimeEnd
      1l, // studyProgrammeId
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
    
    Response response = given()
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("abstractStudentId", is(student.getAbstractStudentId().intValue()))
      .body("firstName", is(student.getFirstName()))
      .body("lastName", is(student.getLastName()))
      .body("nickname", is(student.getNickname()))
      .body("additionalInfo", is(student.getAdditionalInfo()))
      .body("nationalityId", is(student.getNationalityId().intValue()))
      .body("languageId", is(student.getLanguageId().intValue()))
      .body("municipalityId", is(student.getMunicipalityId().intValue()))
      .body("schoolId", is(student.getSchoolId().intValue()))
      .body("activityTypeId", is(student.getActivityTypeId().intValue()))
      .body("examinationTypeId", is(student.getExaminationTypeId().intValue()))
      .body("educationalLevelId", is(student.getExaminationTypeId().intValue()))
      .body("studyTimeEnd", is(student.getStudyTimeEnd().toString()))
      .body("studyProgrammeId", is(student.getStudyProgrammeId().intValue()))
      .body("previousStudies", is(student.getPreviousStudies().floatValue()))
      .body("education", is(student.getEducation()))
      .body("lodging", is(student.getLodging()))
      .body("studyStartDate", is(student.getStudyStartDate().toString()))
      .body("studyEndDate", is(student.getStudyEndDate().toString()))
      .body("studyEndReasonId", is(student.getStudyEndReasonId().intValue()))
      .body("studyEndText", is(student.getStudyEndText()))
      .body("variables", allOf(hasEntry("TV1", "text"), hasEntry("TV2", "123")))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is(student.getArchived()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/students/students/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStudents() {
    given()
      .get("/students/students")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("abstractStudentId[0]", is(1))
      .body("firstName[0]", is("Tanya"))
      .body("lastName[0]", is("Test #1"))
      .body("nickname[0]", is("Tanya-T"))
      .body("additionalInfo[0]", is("Testing #1"))
      .body("nationalityId[0]", is(1))
      .body("languageId[0]", is(1))
      .body("municipalityId[0]", is(1))
      .body("schoolId[0]", is(1))
      .body("activityTypeId[0]", is(1))
      .body("examinationTypeId[0]", is(1))
      .body("educationalLevelId[0]", is(1))
      .body("studyTimeEnd[0]", is((String) null))
      .body("studyProgrammeId[0]", is(1))
      .body("previousStudies[0]", is(0f))
      .body("education[0]", is("Education #1"))
      .body("lodging[0]", is(false))
      .body("studyStartDate[0]", is(getDate(2010, 1, 1).toString()))
      .body("studyEndDate[0]", is((String) null))
      .body("studyEndReasonId[0]", is((Integer) null))
      .body("studyEndText[0]", is((String) null))
      .body("variables[0].size()", is(0))
      .body("tags[0].size", is(0))
      .body("archived[0]", is(Boolean.FALSE))

      .body("id[1]", is(2) )
      .body("abstractStudentId[1]", is(2))
      .body("firstName[1]", is("David"))
      .body("lastName[1]", is("Test #2"))
      .body("nickname[1]", is("David-T"))
      .body("additionalInfo[1]", is("Testing #2"))
      .body("nationalityId[1]", is(1))
      .body("languageId[1]", is(1))
      .body("municipalityId[1]", is(1))
      .body("schoolId[1]", is(1))
      .body("activityTypeId[1]", is(1))
      .body("examinationTypeId[1]", is(1))
      .body("educationalLevelId[1]", is(1))
      .body("studyTimeEnd[1]", is((String) null))
      .body("studyProgrammeId[1]", is(1))
      .body("previousStudies[1]", is(0f))
      .body("education[1]", is("Education #2"))
      .body("lodging[1]", is(false))
      .body("studyStartDate[1]", is(getDate(2010, 1, 1).toString()))
      .body("studyEndDate[1]", is((String) null))
      .body("studyEndReasonId[1]", is((Integer) null))
      .body("studyEndText[1]", is((String) null))
      .body("variables[1].size()", is(0))
      .body("tags[1].size", is(0))
      .body("archived[1]", is(Boolean.FALSE));
  }
  
  @Test
  public void testFindStudent() {
    given()
      .get("/students/students/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("abstractStudentId", is(1))
      .body("firstName", is("Tanya"))
      .body("lastName", is("Test #1"))
      .body("nickname", is("Tanya-T"))
      .body("additionalInfo", is("Testing #1"))
      .body("nationalityId", is(1))
      .body("languageId", is(1))
      .body("municipalityId", is(1))
      .body("schoolId", is(1))
      .body("activityTypeId", is(1))
      .body("examinationTypeId", is(1))
      .body("educationalLevelId", is(1))
      .body("studyTimeEnd", is((String) null))
      .body("studyProgrammeId", is(1))
      .body("previousStudies", is(0f))
      .body("education", is("Education #1"))
      .body("lodging", is(false))
      .body("studyStartDate", is(getDate(2010, 1, 1).toString()))
      .body("studyEndDate", is((String) null))
      .body("studyEndReasonId", is((Integer) null))
      .body("studyEndText", is((String) null))
      .body("variables.size()", is(0))
      .body("tags.size", is(0))
      .body("archived", is(Boolean.FALSE));
  }
  
  @Test
  public void testUpdateStudent() {
    Map<String, String> variables = new HashMap<String, String>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = new Student(null, 
      1l, 
      "not updated firstName", // firstName
      "not updated lastName", // lastName
      "not updated nickname", // nickname
      "not updated additional", // additionalInfo 
      1l, // nationalityId 
      1l, //languageId
      1l, //municipalityId
      1l, // schoolId
      1l, // activityTypeId
      1l, // examinationTypeId
      1l, // educationalLevelId
      getDate(2020, 11, 2), // studyTimeEnd
      1l, // studyProgrammeId
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
      
    Response response = given()
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("abstractStudentId", is(student.getAbstractStudentId().intValue()))
      .body("firstName", is(student.getFirstName()))
      .body("lastName", is(student.getLastName()))
      .body("nickname", is(student.getNickname()))
      .body("additionalInfo", is(student.getAdditionalInfo()))
      .body("nationalityId", is(student.getNationalityId().intValue()))
      .body("languageId", is(student.getLanguageId().intValue()))
      .body("municipalityId", is(student.getMunicipalityId().intValue()))
      .body("schoolId", is(student.getSchoolId().intValue()))
      .body("activityTypeId", is(student.getActivityTypeId().intValue()))
      .body("examinationTypeId", is(student.getExaminationTypeId().intValue()))
      .body("educationalLevelId", is(student.getExaminationTypeId().intValue()))
      .body("studyTimeEnd", is(student.getStudyTimeEnd().toString()))
      .body("studyProgrammeId", is(student.getStudyProgrammeId().intValue()))
      .body("previousStudies", is(student.getPreviousStudies().floatValue()))
      .body("education", is(student.getEducation()))
      .body("lodging", is(student.getLodging()))
      .body("studyStartDate", is(student.getStudyStartDate().toString()))
      .body("studyEndDate", is(student.getStudyEndDate().toString()))
      .body("studyEndReasonId", is(student.getStudyEndReasonId().intValue()))
      .body("studyEndText", is(student.getStudyEndText()))
      .body("variables", allOf(hasEntry("TV1", "text"), hasEntry("TV2", "123")))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is(student.getArchived()));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Map<String, String> updateVariables = new HashMap<String, String>();
      updateVariables.put("TV2", "abc");
      updateVariables.put("TV3", "edf");
      
      Student updateStudent = new Student(id, 
        2l, 
        "updated firstName", // firstName
        "updated lastName", // lastName
        "updated nickname", // nickname
        "updated additional", // additionalInfo 
        2l, // nationalityId 
        2l, //languageId
        2l, //municipalityId
        2l, // schoolId
        2l, // activityTypeId
        2l, // examinationTypeId
        2l, // educationalLevelId
        getDate(2030, 11, 2), // studyTimeEnd
        1l, // studyProgrammeId
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
      
      given()
        .contentType("application/json")
        .body(updateStudent)
        .put("/students/students/{ID}", id)
        .then()
        .body("id", is(updateStudent.getId().intValue()))
        .body("abstractStudentId", is(updateStudent.getAbstractStudentId().intValue()))
        .body("firstName", is(updateStudent.getFirstName()))
        .body("lastName", is(updateStudent.getLastName()))
        .body("nickname", is(updateStudent.getNickname()))
        .body("additionalInfo", is(updateStudent.getAdditionalInfo()))
        .body("nationalityId", is(updateStudent.getNationalityId().intValue()))
        .body("languageId", is(updateStudent.getLanguageId().intValue()))
        .body("municipalityId", is(updateStudent.getMunicipalityId().intValue()))
        .body("schoolId", is(updateStudent.getSchoolId().intValue()))
        .body("activityTypeId", is(updateStudent.getActivityTypeId().intValue()))
        .body("examinationTypeId", is(updateStudent.getExaminationTypeId().intValue()))
        .body("educationalLevelId", is(updateStudent.getExaminationTypeId().intValue()))
        .body("studyTimeEnd", is(updateStudent.getStudyTimeEnd().toString()))
        .body("studyProgrammeId", is(updateStudent.getStudyProgrammeId().intValue()))
        .body("previousStudies", is(updateStudent.getPreviousStudies().floatValue()))
        .body("education", is(updateStudent.getEducation()))
        .body("lodging", is(updateStudent.getLodging()))
        .body("studyStartDate", is(updateStudent.getStudyStartDate().toString()))
        .body("studyEndDate", is(updateStudent.getStudyEndDate().toString()))
        .body("studyEndReasonId", is(updateStudent.getStudyEndReasonId().intValue()))
        .body("studyEndText", is(updateStudent.getStudyEndText()))
        .body("variables", allOf(hasEntry("TV2", "abc"), hasEntry("TV3", "edf")))
        .body("tags", allOf(hasItem("tag2"), hasItem("tag3") ))
        .body("archived", is(updateStudent.getArchived()));

    } finally {
      given()
        .delete("/students/students/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudent() {
    Map<String, String> variables = new HashMap<String, String>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    Student student = new Student(null, 
      1l, 
      "to be deleted", // firstName
      "to be deleted", // lastName
      "to be deleted", // nickname
      "to be deleted", // additionalInfo 
      1l, // nationalityId 
      1l, //languageId
      1l, //municipalityId
      1l, // schoolId
      1l, // activityTypeId
      1l, // examinationTypeId
      1l, // educationalLevelId
      getDate(2020, 11, 2), // studyTimeEnd
      1l, // studyProgrammeId
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
    
    Response response = given()
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/students/students/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/students/students/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/students/students/{ID}", id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/students/students/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().get("/students/students/{ID}", id)
      .then()
      .statusCode(404);
  }
}
