package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.CourseComponent;

public class CourseComponentTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseComponent() {
    Long courseId = 1001l;
    
    CourseComponent courseComponent = new CourseComponent(
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given()
      .contentType("application/json")
      .body(courseComponent)
      .post("/courses/courses/{COURSEID}/components", courseId);
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(courseComponent.getName()))
      .body("description", is( courseComponent.getDescription() ))
      .body("length", is( courseComponent.getLength().floatValue() ))
      .body("lengthUnitId", is( courseComponent.getLengthUnitId().intValue() ))
      .body("archived", is( courseComponent.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListCourseComponents() {
    given()
      .get("/courses/courses/1000/components")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1000) )
      .body("name[0]", is("Test Course #1 component #1" ))
      .body("description[0]", is( "Course component for testing" ))
      .body("length[0]", is( 234f ))
      .body("lengthUnitId[0]", is( 1 ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(1001) )
      .body("name[1]", is("Test Course #1 component #2" ))
      .body("description[1]", is( "Course component for testing" ))
      .body("length[1]", is( 345f ))
      .body("lengthUnitId[1]", is( 1 ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindCourseComponent() {
    given()
      .get("/courses/courses/1001/components/1003")
      .then()
      .statusCode(200)
      .body("id", is(1003) )
      .body("name", is("Test Course #2 component #2" ))
      .body("description", is( "Course component for testing" ))
      .body("length", is( 567f ))
      .body("lengthUnitId", is( 1 ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateCourseComponent() {
    Long courseId = 1001l;
    
    CourseComponent courseComponent = new CourseComponent(
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given()
      .contentType("application/json")
      .body(courseComponent)
      .post("/courses/courses/{COURSEID}/components", courseId);
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    try {
      response.then()
        .body("id", not(is((Long) null)))
        .body("name", is(courseComponent.getName()))
        .body("description", is( courseComponent.getDescription() ))
        .body("length", is( courseComponent.getLength().floatValue() ))
        .body("lengthUnitId", is( courseComponent.getLengthUnitId().intValue() ))
        .body("archived", is( courseComponent.getArchived() ));
      
      CourseComponent updateComponent = new CourseComponent(
          id,
          "Updated name", 
          "Updated description",
          132d, 
          1l, 
          Boolean.FALSE);

      given()
        .contentType("application/json")
        .body(updateComponent)
        .put("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id)
        .then()
        .statusCode(200)
        .body("id", is(updateComponent.getId().intValue()))
        .body("name", is(updateComponent.getName()))
        .body("description", is( updateComponent.getDescription() ))
        .body("length", is( updateComponent.getLength().floatValue() ))
        .body("lengthUnitId", is( updateComponent.getLengthUnitId().intValue() ))
        .body("archived", is( updateComponent.getArchived() ));  

    } finally {
      given()
        .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteCourseComponent() {
    Long courseId = 1001l;
    
    CourseComponent courseComponent = new CourseComponent(
        "Create test component", 
        "Component for testing creating of the component",
        12d, 
        1l, 
        Boolean.FALSE);

    Response response = given()
      .contentType("application/json")
      .body(courseComponent)
      .post("/courses/courses/{COURSEID}/components", courseId);
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id)
      .then()
      .statusCode(204);
    
    given().get("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id)
      .then()
      .statusCode(404);
    
    given()
      .delete("/courses/courses/{COURSEID}/components/{COMPONENTID}?permanent=true", courseId, id)
      .then()
      .statusCode(204);
    
    
    given().get("/courses/courses/{COURSEID}/components/{COMPONENTID}", courseId, id)
      .then()
      .statusCode(404);
  }
}
