package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.CourseType;

public class CourseTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseType() {
    CourseType courseType = new CourseType("Test Type", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseType)
      .post("/courses/courseTypes");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseType.getName()))
      .body("archived", is( courseType.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/courses/courseTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListCourseTypes() {
    given().headers(getAuthHeaders())
      .get("/courses/courseTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Non-stop" ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Group Work" ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindCourseType() {
    given().headers(getAuthHeaders())
      .get("/courses/courseTypes/{ID}", 2)
      .then()
      .statusCode(200)
      .body("id", is(2) )
      .body("name", is("Group Work" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateCourseType() {
    CourseType courseType = new CourseType(
        "Update test", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseType)
      .post("/courses/courseTypes");
     
    Long id = response.body().jsonPath().getLong("id");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseType.getName()))
      .body("archived", is( courseType.getArchived() ));
    
    CourseType updateType = new CourseType(
        id,
        "Updated name",
        Boolean.FALSE);

    given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(updateType)
      .put("/courses/courseTypes/{ID}", id)
      .then()
      .statusCode(200)
      .body("id", is(updateType.getId().intValue()))
      .body("name", is(updateType.getName()))
      .body("archived", is( updateType.getArchived() ));  

    given().headers(getAuthHeaders())
      .delete("/courses/courseTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testDeleteCourseType() {
    CourseType courseType = new CourseType("Delete Test", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(courseType)
      .post("/courses/courseTypes");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseType.getName()))
      .body("archived", is( courseType.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/courses/courseTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courseTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/courseTypes/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courseTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);  
    
    given().headers(getAuthHeaders()).get("/courses/courseTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
