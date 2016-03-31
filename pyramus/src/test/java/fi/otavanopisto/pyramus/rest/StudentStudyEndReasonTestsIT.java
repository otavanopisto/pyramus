package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.StudentStudyEndReason;

public class StudentStudyEndReasonTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudyEndReason() {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "create test", null);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(endReason.getName()))
      .body("parentId", is((Long) null));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/studyEndReasons/{ID}", id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListStudyEndReasons() {
    given().headers(getAuthHeaders())
      .get("/students/studyEndReasons")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("StudentStudyEndReason #1"))
      .body("parentReasonId[0]", is((Long) null))
      .body("id[1]", is(2) )
      .body("name[1]", is("StudentStudyEndReason #2"))
      .body("parentReasonId[1]", is(1));
  }

  @Test
  public void testFindStudyEndReasons() {
    given().headers(getAuthHeaders())
      .get("/students/studyEndReasons/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("StudentStudyEndReason #1"))
      .body("parentReasonId", is((Long) null));
  }
  
  @Test
  public void testUpdateStudyEndReason() {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "not updated", null);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(endReason.getName()))
      .body("parentReasonId", is((Long) null));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentStudyEndReason updateReason = new StudentStudyEndReason(id, "updated", 1l);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateReason)
        .put("/students/studyEndReasons/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateReason.getId().intValue() ))
        .body("name", is(updateReason.getName()))
        .body("parentReasonId", is(updateReason.getParentReasonId().intValue()));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/studyEndReasons/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudyEndReason() {
    StudentStudyEndReason endReason = new StudentStudyEndReason(null, "to be deleted", null);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(endReason)
      .post("/students/studyEndReasons");
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/studyEndReasons/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/studyEndReasons/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/studyEndReasons/{ID}", id)
      .then()
      .statusCode(404);
  }
}
