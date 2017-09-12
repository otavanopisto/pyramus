package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Curriculum;

public class CurriculumTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCurriculum() {
    Curriculum curriculum = new Curriculum(null, "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(curriculum)
      .post("/common/curriculums");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(curriculum.getName()))
      .body("archived", is(curriculum.getArchived()));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/common/curriculums/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listCurriculums() {
    given().headers(getAuthHeaders())
      .get("/common/curriculums")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Curriculum #1"))
      .body("archived[0]", is(false))
      .body("id[1]", is(2) )
      .body("name[1]", is("Curriculum #2"))
      .body("archived[1]", is(false));
  }
  
  @Test
  public void testFindCurriculum() {
    given().headers(getAuthHeaders())
      .get("/common/curriculums/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Curriculum #1"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateCurriculum() {
    Curriculum curriculum = new Curriculum(null, "Not Updated", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(curriculum)
      .post("/common/curriculums");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(curriculum.getName()))
      .body("archived", is(curriculum.getArchived()));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Curriculum updateCurriculum = new Curriculum(id, "Updated", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateCurriculum)
        .put("/common/curriculums/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateCurriculum.getId().intValue()))
        .body("name", is(updateCurriculum.getName()))
        .body("archived", is(updateCurriculum.getArchived()));
    } finally {
      given().headers(getAuthHeaders())
        .delete("/common/curriculums/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteCurriculum() {
    Curriculum curriculum = new Curriculum(null, "create type", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(curriculum)
      .post("/common/curriculums");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders())
      .get("/common/curriculums/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/common/curriculums/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/common/curriculums/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/common/curriculums/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .get("/common/curriculums/{ID}", id)
      .then()
      .statusCode(404);
  }
}
