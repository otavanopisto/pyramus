package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.CourseState;

public class CourseStateTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseState() {
    CourseState courseState = new CourseState("Test State", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseState.getName()))
      .body("archived", is( courseState.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/courses/courseStates/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListCourseStates() {
    given().headers(getAuthHeaders())
      .get("/courses/courseStates")
      .then()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[0]", is(1) )
      .body("name[0]", is("Planning" ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("In Progress" ))
      .body("archived[1]", is( false ))
      .body("id[2]", is(3) )
      .body("name[2]", is("Ended" ))
      .body("archived[2]", is( false ));
  }
  
  @Test
  public void testFindCourseState() {
    given().headers(getAuthHeaders())
      .get("/courses/courseStates/{ID}", 3)
      .then()
      .statusCode(200)
      .body("id", is(3) )
      .body("name", is("Ended" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateCourseState() {
    CourseState courseState = new CourseState(
        "Update test", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");
     
    Long id = new Long(response.body().jsonPath().getInt("id"));

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseState.getName()))
      .body("archived", is( courseState.getArchived() ));
    
    CourseState updateState = new CourseState(
        id,
        "Updated name",
        Boolean.FALSE);

    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateState)
      .put("/courses/courseStates/{ID}", id)
      .then()
      .statusCode(200)
      .body("id", is(updateState.getId().intValue()))
      .body("name", is(updateState.getName()))
      .body("archived", is( updateState.getArchived() ));  

    given().headers(getAuthHeaders())
      .delete("/courses/courseStates/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourseComponent() {
    CourseState courseState = new CourseState("Delete Test", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseState)
      .post("/courses/courseStates");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseState.getName()))
      .body("archived", is( courseState.getArchived() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/courses/courseStates/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courseStates/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/courseStates/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courseStates/{ID}?permanent=true", id)
      .then()
      .statusCode(204);  
    
    given().headers(getAuthHeaders()).get("/courses/courseStates/{ID}", id)
      .then()
      .statusCode(404);
  }
}
