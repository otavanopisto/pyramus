package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.CourseEnrolmentType;

public class CourseEnrolmentTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseEnrolmentType() {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Test Enrolment Type");

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(courseEnrolmentType.getName()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListCourseEnrolmentTypes() {
    given().headers(getAuthHeaders())
      .get("/courses/enrolmentTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Manual"))
      .body("id[1]", is(2) )
      .body("name[1]", is("LE"));
  }
  
  @Test
  public void testFindCourseEnrolmentType() {
    given().headers(getAuthHeaders())
      .get("/courses/enrolmentTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Manual"));
  }
  
  @Test
  public void testUpdateCourseState() {
    CourseEnrolmentType enrolmentType = new CourseEnrolmentType("Update test");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(enrolmentType)
      .post("/courses/enrolmentTypes");
     
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(enrolmentType.getName()));
    
    Long id = response.body().jsonPath().getLong("id");

    CourseEnrolmentType updateEnrolmentType = new CourseEnrolmentType(id, "Updated name");

    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateEnrolmentType)
      .put("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(200)
      .body("id", is(updateEnrolmentType.getId().intValue()))
      .body("name", is(updateEnrolmentType.getName()));

    given().headers(getAuthHeaders())
      .delete("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourseEnrolmentType() {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Delete Test");

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseEnrolmentType.getName()));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
