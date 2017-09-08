package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.StudentActivityType;

public class StudentActivityTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentActivityType() {
    StudentActivityType studentActivityType = new StudentActivityType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studentActivityType.getName()))
      .body("archived", is( studentActivityType.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/activityTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listStudentActivityTypes() {
    given().headers(getAuthHeaders())
      .get("/students/activityTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("StudentActivityType #1" ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("StudentActivityType #2" ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindStudentActivityType() {
    given().headers(getAuthHeaders())
      .get("/students/activityTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("StudentActivityType #1" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateStudentActivityType() {
    StudentActivityType studentActivityType = new StudentActivityType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studentActivityType.getName()))
      .body("archived", is( studentActivityType.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentActivityType updateStudentActivityType = new StudentActivityType(id, "Updated", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudentActivityType)
        .put("/students/activityTypes/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateStudentActivityType.getId().intValue() ))
        .body("name", is(updateStudentActivityType.getName()))
        .body("archived", is( updateStudentActivityType.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/activityTypes/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentActivityType() {
    StudentActivityType studentActivityType = new StudentActivityType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentActivityType)
      .post("/students/activityTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/activityTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/activityTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/activityTypes/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/activityTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/activityTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
