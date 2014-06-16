package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.CourseEnrolmentType;

public class CourseEnrolmentTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseEnrolmentType() {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Test Enrolment Type");

    Response response = given()
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(courseEnrolmentType.getName()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListCourseEnrolmentTypes() {
    given()
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
    given()
      .get("/courses/enrolmentTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Manual"));
  }
  
  @Test
  public void testUpdateCourseState() {
    CourseEnrolmentType enrolmentType = new CourseEnrolmentType("Update test");
    
    Response response = given()
      .contentType("application/json")
      .body(enrolmentType)
      .post("/courses/enrolmentTypes");
     
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(enrolmentType.getName()));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));

    CourseEnrolmentType updateEnrolmentType = new CourseEnrolmentType(id, "Updated name");

    given()
      .contentType("application/json")
      .body(updateEnrolmentType)
      .put("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(200)
      .body("id", is(updateEnrolmentType.getId().intValue()))
      .body("name", is(updateEnrolmentType.getName()));

    given()
      .delete("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourseEnrolmentType() {
    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType("Delete Test");

    Response response = given()
      .contentType("application/json")
      .body(courseEnrolmentType)
      .post("/courses/enrolmentTypes");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseEnrolmentType.getName()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().get("/courses/enrolmentTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
