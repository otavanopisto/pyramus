package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.AbstractStudent;
import fi.pyramus.rest.model.Sex;

public class AbstractStudentTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateAbstractStudent() {
    AbstractStudent abstractStudent = new AbstractStudent(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be created");
    
    Response response = given()
      .contentType("application/json")
      .body(abstractStudent)
      .post("/students/abstractStudents");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(abstractStudent.getBirthday().toString()))
      .body("socialSecurityNumber", is(abstractStudent.getSocialSecurityNumber() ))
      .body("basicInfo", is(abstractStudent.getBasicInfo() ))
      .body("secureInfo", is(abstractStudent.getSecureInfo() ))
      .body("sex", is(abstractStudent.getSex().toString() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/students/abstractStudents/{ID}", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListAbstractStudents() {
    given()
      .get("/students/abstractStudents")
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
  public void testFindAbstractStudent() {
    given()
      .get("/students/abstractStudents/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("birthday", is(getDate(1990, 1, 1).toString() ))
      .body("sex", is("FEMALE") )
      .body("socialSecurityNumber", is("123456-7890") )
      .body("secureInfo", is(Boolean.FALSE) );
  }
  
  @Test
  public void testUpdateAbstractStudent() {
    AbstractStudent abstractStudent = new AbstractStudent(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "not updated");
    
    Response response = given()
      .contentType("application/json")
      .body(abstractStudent)
      .post("/students/abstractStudents");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(abstractStudent.getBirthday().toString()))
      .body("socialSecurityNumber", is(abstractStudent.getSocialSecurityNumber() ))
      .body("basicInfo", is(abstractStudent.getBasicInfo() ))
      .body("secureInfo", is(abstractStudent.getSecureInfo() ))
      .body("sex", is(abstractStudent.getSex().toString() ));
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      AbstractStudent updateStudent = new AbstractStudent(id, getDate(1991, 7, 7), "1234567-9876", Sex.MALE, true, "updated");

      given()
        .contentType("application/json")
        .body(updateStudent)
        .put("/students/abstractStudents/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateStudent.getId().intValue() ))
        .body("birthday", is(updateStudent.getBirthday().toString()))
        .body("socialSecurityNumber", is(updateStudent.getSocialSecurityNumber() ))
        .body("basicInfo", is(updateStudent.getBasicInfo() ))
        .body("secureInfo", is(updateStudent.getSecureInfo() ))
        .body("sex", is(updateStudent.getSex().toString() ));

    } finally {
      given()
        .delete("/students/abstractStudents/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteAbstractStudent() {
    AbstractStudent abstractStudent = new AbstractStudent(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be deleted");
    
    Response response = given()
      .contentType("application/json")
      .body(abstractStudent)
      .post("/students/abstractStudents");

    response.then()
      .body("id", not(is((Long) null)))
      .body("birthday", is(abstractStudent.getBirthday().toString()))
      .body("socialSecurityNumber", is(abstractStudent.getSocialSecurityNumber() ))
      .body("basicInfo", is(abstractStudent.getBasicInfo() ))
      .body("secureInfo", is(abstractStudent.getSecureInfo() ))
      .body("sex", is(abstractStudent.getSex().toString() ));
    int id = response.body().jsonPath().getInt("id");
    assertNotNull(id);
    
    given().get("/students/abstractStudents/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/students/abstractStudents/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/students/abstractStudents/{ID}", id)
      .then()
      .statusCode(404);
  }
}
