package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Email;
import fi.pyramus.rest.model.Person;
import fi.pyramus.rest.model.Sex;
import fi.pyramus.rest.model.Student;
import fi.pyramus.rest.model.UserRole;

public class PersonTestsIT extends AbstractRESTServiceTest {

  // TODO: tests for default person

  @Test
  public void testCreatePerson() {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be created", null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(person.getBirthday().toString()))
      .body("socialSecurityNumber", is(person.getSocialSecurityNumber() ))
      .body("basicInfo", is(person.getBasicInfo() ))
      .body("secureInfo", is(person.getSecureInfo() ))
      .body("sex", is(person.getSex().toString() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/persons/persons/{ID}", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListPersons() {
    given().headers(getAuthHeaders())
      .get("/persons/persons")
      .then()
      .statusCode(200)
      .body("id.size()", is(11))
      .body("id[2]", is(3) )
      .body("birthday[2]", is(getDate(1990, 1, 1).toString() ))
      .body("sex[2]", is("FEMALE") )
      .body("socialSecurityNumber[2]", is("123456-7890") )
      .body("secureInfo[2]", is(Boolean.FALSE) )
      .body("id[3]", is(4) )
      .body("birthday[3]", is(getDate(1990, 1, 1).toString() ))
      .body("sex[3]", is("MALE") )
      .body("socialSecurityNumber[3]", is("01234567-8901") )
      .body("secureInfo[3]", is(Boolean.FALSE) );
  }
  
  @Test
  public void testListPersonsLimit() {
    given().headers(getAuthHeaders())
      .get("/persons/persons?firstResult=2&maxResults=3")
      .then()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[0]", is(3) )
      .body("birthday[0]", is(getDate(1990, 1, 1).toString() ))
      .body("sex[0]", is( Sex.FEMALE.name() ) )
      .body("socialSecurityNumber[0]", is("123456-7890") )
      .body("secureInfo[0]", is(Boolean.FALSE) )
      .body("id[1]", is(4) )
      .body("birthday[1]", is(getDate(1990, 1, 1).toString() ))
      .body("sex[1]", is( Sex.MALE.name() ) )
      .body("socialSecurityNumber[1]", is("01234567-8901") )
      .body("secureInfo[1]", is(Boolean.FALSE) );
  }
  
  @Test
  public void testFindPerson() {
    given().headers(getAuthHeaders())
      .get("/persons/persons/{ID}", 3)
      .then()
      .statusCode(200)
      .body("id", is(3) )
      .body("birthday", is(getDate(1990, 1, 1).toString() ))
      .body("sex", is("FEMALE") )
      .body("socialSecurityNumber", is("123456-7890") )
      .body("secureInfo", is(Boolean.FALSE) );
  }
  
  @Test
  public void testUpdatePerson() {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "not updated", null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(person.getBirthday().toString()))
      .body("socialSecurityNumber", is(person.getSocialSecurityNumber() ))
      .body("basicInfo", is(person.getBasicInfo() ))
      .body("secureInfo", is(person.getSecureInfo() ))
      .body("sex", is(person.getSex().toString() ));
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Person updateStudent = new Person(id, getDate(1991, 7, 7), "1234567-9876", Sex.MALE, true, "updated", null);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudent)
        .put("/persons/persons/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateStudent.getId().intValue() ))
        .body("birthday", is(updateStudent.getBirthday().toString()))
        .body("socialSecurityNumber", is(updateStudent.getSocialSecurityNumber() ))
        .body("basicInfo", is(updateStudent.getBasicInfo() ))
        .body("secureInfo", is(updateStudent.getSecureInfo() ))
        .body("sex", is(updateStudent.getSex().toString() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/persons/persons/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeletePerson() {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be deleted", null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(person.getBirthday().toString()))
      .body("socialSecurityNumber", is(person.getSocialSecurityNumber() ))
      .body("basicInfo", is(person.getBasicInfo() ))
      .body("secureInfo", is(person.getSecureInfo() ))
      .body("sex", is(person.getSex().toString() ));
    int id = response.body().jsonPath().getInt("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders())
      .get("/persons/persons/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/persons/persons/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/persons/persons/{ID}", id)
      .then()
      .statusCode(404);
  }
  
  @Test
  public void testListStudents() {
    given().headers(getAuthHeaders())   
      .get("/persons/persons/{ID}/students", 3l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(3) )
      .body("personId[0]", is(3))
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
      .body("archived[0]", is(Boolean.FALSE));
  }
  
  @Test
  public void testEmailUniquityRestriction() {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be created", null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    int person1Id = response.body().jsonPath().getInt("id");

    response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    int person2Id = response.body().jsonPath().getInt("id");
    
    Map<String, String> variables = new HashMap<String, String>();
    
    Student student = new Student(null, 
      (long) person1Id, // personId
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
      2d, // previousStudies
      "Carpenter", // education
      Boolean.FALSE, // lodging
      getDate(2010, 2, 3), // studyStartDate
      getDate(2013, 1, 2), // studyEndDate
      1l, // studyEndReasonId, 
      "Testing...", // studyEndText, 
      variables, // variables
      new ArrayList<String>(),  // tags, 
      Boolean.FALSE //archived
    );
      
    response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(student)
      .post("/students/students");
    
    int student1Id = response.body().jsonPath().getInt("id");

    response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(student)
        .post("/students/students");
      
    int student2Id = response.body().jsonPath().getInt("id");
    
    student.setPersonId((long) person2Id);

    response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(student)
        .post("/students/students");
      
    int student3Id = response.body().jsonPath().getInt("id");
    
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/students/students/{ID}/emails", student1Id);

    response.then()
      .statusCode(200);
      
    int email1id = response.body().jsonPath().getInt("id");
    
    response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/students/students/{ID}/emails", student2Id);

    // Same email to same person is ok
    response.then()
      .statusCode(200);
      
    int email2id = response.body().jsonPath().getInt("id");
    
    response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(email)
      .post("/students/students/{ID}/emails", student3Id);

    // Same email to different person is not ok
    response.then()
      .statusCode(403);
      
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/emails/{ID}", student2Id, email2id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/emails/{ID}", student1Id, email1id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{ID}?permanent=true", student3Id)
      .then()
      .statusCode(204);

    given().headers(getAuthHeaders())
      .delete("/students/students/{ID}?permanent=true", student2Id)
      .then()
      .statusCode(204);

    given().headers(getAuthHeaders())
      .delete("/students/students/{ID}?permanent=true", student1Id)
      .then()
      .statusCode(204);

    given().headers(getAuthHeaders())
      .delete("/persons/persons/{ID}", person1Id)
      .then()
      .statusCode(204);
  
    given().headers(getAuthHeaders())
      .delete("/persons/persons/{ID}", person2Id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListPersonStaffMembers() {
    given().headers(getAuthHeaders())
      .get("/persons/persons/{ID}/staffMembers", 1)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1))
      .body("firstName[0]", is("Test Guest"))
      .body("lastName[0]", is("User #1"))
      .body("role[0]", is(UserRole.GUEST.name()));
  }
}
