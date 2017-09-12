package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.SchoolField;

public class SchoolFieldTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateSchoolField() {
    SchoolField schoolField = new SchoolField(null, "to be created", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(schoolField.getName()))
      .body("archived", is( schoolField.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/schools/schoolFields/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListSchoolFields() {
    given().headers(getAuthHeaders())
      .get("/schools/schoolFields")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Field #1"))
      .body("archived[0]", is(Boolean.FALSE))
      .body("id[1]", is(2) )
      .body("name[1]", is("Field #2"))
      .body("archived[1]", is(Boolean.FALSE));
  }
  
  @Test
  public void testFindSchoolField() {
    given().headers(getAuthHeaders())
      .get("/schools/schoolFields/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Field #1"))
      .body("archived", is(Boolean.FALSE));
  }
  
  @Test
  public void testUpdateSchoolField() {
    SchoolField schoolField = new SchoolField(null, "not updated", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(schoolField.getName()))
      .body("archived", is(schoolField.getArchived()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      SchoolField updateSchoolField = new SchoolField(id, "updated", Boolean.FALSE);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateSchoolField)
        .put("/schools/schoolFields/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateSchoolField.getId().intValue()))
        .body("name", is(updateSchoolField.getName()))
        .body("archived", is(updateSchoolField.getArchived()));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/schools/schoolFields/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteSchoolField() {
    SchoolField schoolField = new SchoolField(null, "to be deleted", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(schoolField)
      .post("/schools/schoolFields");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(schoolField.getName()))
      .body("archived", is(schoolField.getArchived()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/schools/schoolFields/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/schools/schoolFields/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/schools/schoolFields/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/schools/schoolFields/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/schools/schoolFields/{ID}", id)
      .then()
      .statusCode(404);
  }
}
