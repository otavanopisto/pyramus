package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.CourseParticipationType;

public class CourseParticipationTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseParticipationType() {
    CourseParticipationType courseParticipationType = new CourseParticipationType("Test Type", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseParticipationType.getName()))
      .body("archived", is( courseParticipationType.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/courses/participationTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListCourseParticipationTypes() {
    given().headers(getAuthHeaders())
      .get("/courses/participationTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Canceled" ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Passed" ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testListCourseParticipationType() {
    given().headers(getAuthHeaders())
      .get("/courses/participationTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Canceled" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateCourseParticipationType() {
    CourseParticipationType courseParticipationType = new CourseParticipationType(
        "Update test", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");
     
    Long id = response.body().jsonPath().getLong("id");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseParticipationType.getName()))
      .body("archived", is( courseParticipationType.getArchived() ));
    
    CourseParticipationType updateType = new CourseParticipationType(
        id,
        "Updated name",
        Boolean.FALSE);

    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateType)
      .put("/courses/participationTypes/{ID}", id)
      .then()
      .statusCode(200)
      .body("id", is(updateType.getId().intValue()))
      .body("name", is(updateType.getName()))
      .body("archived", is( updateType.getArchived() ));  

    given().headers(getAuthHeaders())
      .delete("/courses/participationTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourseParticipationType() {
    CourseParticipationType courseParticipationType = new CourseParticipationType("Delete Type", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseParticipationType)
      .post("/courses/participationTypes");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseParticipationType.getName()))
      .body("archived", is( courseParticipationType.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/courses/participationTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/participationTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/participationTypes/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/courses/participationTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);  
    
    given().headers(getAuthHeaders()).get("/courses/participationTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
