package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.ContactURL;

public class StudentContactURLTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentContactURL() {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/students/students/{ID}/contactURLs", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("url", is(contactURL.getUrl()))
      .body("contactURLTypeId", is(contactURL.getContactURLTypeId().intValue()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactURLs/{ID}", 1l, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStudentContactURLs() {
    given().headers(getAuthHeaders())
      .get("/students/students/{ID}/contactURLs", 1l)
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(3) )
      .body("url[0]", is("http://www.student1webpage.com"))
      .body("contactURLTypeId[0]", is(1));
  }
  
  @Test
  public void testFindStudentContactURL() {
    given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/contactURLs/{ID}", 1l, 3l)
      .then()
      .statusCode(200)
      .body("id", is(3) )
      .body("url", is("http://www.student1webpage.com"))
      .body("contactURLTypeId", is(1));
  }  

  @Test
  public void testDeleteStudentContactURL() {
    ContactURL contactURL = new ContactURL(null, 1l, "http://www.myfakehomepage.org");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(contactURL)
      .post("/students/students/{STUDENTID}/contactURLs", 1l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("url", is(contactURL.getUrl()))
      .body("contactURLTypeId", is(contactURL.getContactURLTypeId().intValue()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/contactURLs/{ID}", 1l, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactURLs/{ID}", 1l, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/students/{STUDENTID}/contactURLs/{ID}", 1l, id)
      .then()
      .statusCode(404);
  }
}
