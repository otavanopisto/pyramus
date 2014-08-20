package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.Email;

public class StudentEmailTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentEmail() {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given()
      .contentType("application/json")
      .body(email)
      .post("/students/students/{ID}/emails", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("address", is(email.getAddress()))
      .body("contactTypeId", is(email.getContactTypeId().intValue()))
      .body("defaultAddress", is( email.getDefaultAddress()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/students/students/{STUDENTID}/emails/{ID}", 1l, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStudentEmails() {
    given()
      .get("/students/students/{ID}/emails", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(3) )
      .body("address[0]", is("student1@bogusmail.com"))
      .body("contactTypeId[0]", is(1))
      .body("defaultAddress[0]", is(Boolean.TRUE));
  }
  
  @Test
  public void testFindStudentEmail() {
    given()
      .get("/students/students/{STUDENTID}/emails/{ID}", 1l, 3l)
      .then()
      .statusCode(200)
      .body("id", is(3) )
      .body("address", is("student1@bogusmail.com"))
      .body("contactTypeId", is(1))
      .body("defaultAddress", is(Boolean.TRUE));
  }  

  @Test
  public void testDeleteStudentEmail() {
    Email email = new Email(null, 1l, Boolean.FALSE, "bogus@norealmail.org");
    
    Response response = given()
      .contentType("application/json")
      .body(email)
      .post("/students/students/{STUDENTID}/emails", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("address", is(email.getAddress()))
      .body("contactTypeId", is(email.getContactTypeId().intValue()))
      .body("defaultAddress", is( email.getDefaultAddress()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/students/students/{STUDENTID}/emails/{ID}", 1l, id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/students/students/{STUDENTID}/emails/{ID}", 1l, id)
      .then()
      .statusCode(204);
    
    given().get("/students/students/{STUDENTID}/emails/{ID}", 1l, id)
      .then()
      .statusCode(404);
  }
}
