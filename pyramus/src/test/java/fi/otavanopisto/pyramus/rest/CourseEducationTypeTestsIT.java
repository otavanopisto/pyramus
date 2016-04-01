package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class CourseEducationTypeTestsIT extends AbstractRESTServiceTest {
  
  @Test
  public void testListCourseEducationTypes() {
    given().headers(getAuthHeaders())
      .get("/courses/courses/1001/educationTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1000) )
      .body("educationTypeId[0]", is(1))
      .body("archived[0]", is( false ));
  }
  
  @Test
  public void testListCourseEducationSubtypes() {
    given().headers(getAuthHeaders())
      .get("/courses/courses/1001/educationTypes/1000/educationSubtypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1000) )
      .body("educationSubtypeId[0]", is(1))
      .body("archived[0]", is( false ));
  }
}
