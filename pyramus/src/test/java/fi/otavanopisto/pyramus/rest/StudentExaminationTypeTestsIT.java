package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.StudentExaminationType;

public class StudentExaminationTypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentExaminationType() {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studentExaminationType.getName()))
      .body("archived", is( studentExaminationType.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/examinationTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listStudentExaminationTypes() {
    given().headers(getAuthHeaders())
      .get("/students/examinationTypes")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("StudentExaminationType #1" ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("StudentExaminationType #2" ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindStudentExaminationType() {
    given().headers(getAuthHeaders())
      .get("/students/examinationTypes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("StudentExaminationType #1" ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateStudentExaminationType() {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studentExaminationType.getName()))
      .body("archived", is( studentExaminationType.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentExaminationType updateStudentExaminationType = new StudentExaminationType(id, "Updated", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudentExaminationType)
        .put("/students/examinationTypes/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateStudentExaminationType.getId().intValue() ))
        .body("name", is(updateStudentExaminationType.getName()))
        .body("archived", is( updateStudentExaminationType.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/examinationTypes/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentExaminationType() {
    StudentExaminationType studentExaminationType = new StudentExaminationType(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentExaminationType)
      .post("/students/examinationTypes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/examinationTypes/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/examinationTypes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/examinationTypes/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/examinationTypes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/examinationTypes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
