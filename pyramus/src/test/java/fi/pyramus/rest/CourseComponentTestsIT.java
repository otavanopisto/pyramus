package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class CourseComponentTestsIT extends AbstractRESTServiceTest {

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
  
}
