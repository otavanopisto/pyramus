package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Person;
import fi.pyramus.rest.model.Sex;

public class PersonTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreatePerson() {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be created");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/students/persons");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(person.getBirthday().toString()))
      .body("socialSecurityNumber", is(person.getSocialSecurityNumber() ))
      .body("basicInfo", is(person.getBasicInfo() ))
      .body("secureInfo", is(person.getSecureInfo() ))
      .body("sex", is(person.getSex().toString() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/persons/{ID}", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListPersons() {
    given().headers(getAuthHeaders())
      .get("/students/persons")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("birthday[0]", is(getDate(1990, 1, 1).toString() ))
      .body("sex[0]", is("FEMALE") )
      .body("socialSecurityNumber[0]", is("123456-7890") )
      .body("secureInfo[0]", is(Boolean.FALSE) )
      .body("id[1]", is(2) )
      .body("birthday[1]", is(getDate(1990, 1, 1).toString() ))
      .body("sex[1]", is("MALE") )
      .body("socialSecurityNumber[1]", is("01234567-8901") )
      .body("secureInfo[1]", is(Boolean.FALSE) );
  }
  
  @Test
  public void testFindPerson() {
    given().headers(getAuthHeaders())
      .get("/students/persons/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("birthday", is(getDate(1990, 1, 1).toString() ))
      .body("sex", is("FEMALE") )
      .body("socialSecurityNumber", is("123456-7890") )
      .body("secureInfo", is(Boolean.FALSE) );
  }
  
  @Test
  public void testUpdatePerson() {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "not updated");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/students/persons");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(person.getBirthday().toString()))
      .body("socialSecurityNumber", is(person.getSocialSecurityNumber() ))
      .body("basicInfo", is(person.getBasicInfo() ))
      .body("secureInfo", is(person.getSecureInfo() ))
      .body("sex", is(person.getSex().toString() ));
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Person updateStudent = new Person(id, getDate(1991, 7, 7), "1234567-9876", Sex.MALE, true, "updated");

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudent)
        .put("/students/persons/{ID}", id)
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
        .delete("/students/persons/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeletePerson() {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be deleted");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/students/persons");

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
      .get("/students/persons/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/persons/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/students/persons/{ID}", id)
      .then()
      .statusCode(404);
  }
  
  @Test
  public void testListStudents() {
    given().headers(getAuthHeaders())   
      .get("/students/persons/{ID}/students", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(3) )
      .body("personId[0]", is(1))
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
}
