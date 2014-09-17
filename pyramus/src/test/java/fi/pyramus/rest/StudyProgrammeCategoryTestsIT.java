package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.StudyProgrammeCategory;

public class StudyProgrammeCategoryTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudyProgrammeCategory() {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "create", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studyProgrammeCategory.getName()))
      .body("educationTypeId", is( studyProgrammeCategory.getEducationTypeId().intValue() ))
      .body("archived", is( studyProgrammeCategory.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listStudyProgrammeCategories() {
    given().headers(getAuthHeaders())
      .get("/students/studyProgrammeCategories")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("StudyProgrammeCategory #1" ))
      .body("educationTypeId[0]", is( 1 ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("StudyProgrammeCategory #2" ))
      .body("educationTypeId[1]", is( 2 ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindStudyProgrammeCategory() {
    given().headers(getAuthHeaders())
      .get("/students/studyProgrammeCategories/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("StudyProgrammeCategory #1" ))
      .body("educationTypeId", is( 1 ))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateStudyProgrammeCategory() {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "Not Updated", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studyProgrammeCategory.getName()))
      .body("educationTypeId", is( studyProgrammeCategory.getEducationTypeId().intValue() ))
      .body("archived", is( studyProgrammeCategory.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudyProgrammeCategory updateStudyProgrammeCategory = new StudyProgrammeCategory(id, "Updated", 2l, Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudyProgrammeCategory)
        .put("/students/studyProgrammeCategories/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateStudyProgrammeCategory.getId().intValue() ))
        .body("name", is(updateStudyProgrammeCategory.getName()))
        .body("educationTypeId", is( updateStudyProgrammeCategory.getEducationTypeId().intValue() ))
        .body("archived", is( updateStudyProgrammeCategory.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudyProgrammeCategory() {
    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory(null, "create type", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgrammeCategory)
      .post("/students/studyProgrammeCategories");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/studyProgrammeCategories/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/studyProgrammeCategories/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/studyProgrammeCategories/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/studyProgrammeCategories/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/studyProgrammeCategories/{ID}", id)
      .then()
      .statusCode(404);
  }
}
