package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.otavanopisto.pyramus.rest.model.Organization;
import fi.otavanopisto.pyramus.rest.model.StudyProgramme;
import io.restassured.response.Response;

public class StudyProgrammeTestsIT extends AbstractRESTServiceTest {

  private static Long organizationId;
  
  @Before
  public void initialize() {
    Organization organization = new Organization(null, "Organization.StudyProgrammeTestsIT", false);
    
    Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(organization)
        .post("/organizations");
    response.then().statusCode(200);
    
    organizationId = (long) response.body().jsonPath().getInt("id");
  }
  
  @After
  public void deinitialize() {
    given().headers(getAuthHeaders())
      .delete("/organizations/{ID}?permanent=true", organizationId)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testCreateStudyProgramme() {
    StudyProgramme studyProgramme = new StudyProgramme(null, organizationId, "TST", "create", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("code", is(studyProgramme.getCode()))
      .body("name", is(studyProgramme.getName()))
      .body("categoryId", is(studyProgramme.getCategoryId().intValue()))
      .body("archived", is( studyProgramme.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/studyProgrammes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void listStudyProgrammes() {
    given().headers(getAuthHeaders())
      .get("/students/studyProgrammes")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("StudyProgramme #1"))
      .body("code[0]", is("TST1"))
      .body("categoryId[0]", is(1))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("StudyProgramme #2"))
      .body("code[1]", is("TST2"))
      .body("categoryId[1]", is(2))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindStudyProgramme() {
    given().headers(getAuthHeaders())
      .get("/students/studyProgrammes/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("StudyProgramme #1"))
      .body("code", is("TST1"))
      .body("archived", is( false ))
      .body("categoryId", is(1));
  }
  
  @Test
  public void testUpdateStudyProgramme() {
    StudyProgramme studyProgramme = new StudyProgramme(null, organizationId, "NOT", "Not Updated", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(studyProgramme.getName()))
      .body("code", is(studyProgramme.getCode()))
      .body("archived", is(studyProgramme.getArchived()))
      .body("categoryId", is(studyProgramme.getCategoryId().intValue()));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudyProgramme updateStudyProgramme = new StudyProgramme(id, organizationId, "UPD", "Updated", 2l, Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateStudyProgramme)
        .put("/students/studyProgrammes/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateStudyProgramme.getId().intValue() ))
        .body("name", is(updateStudyProgramme.getName()))
        .body("code", is(updateStudyProgramme.getCode()))
        .body("archived", is(updateStudyProgramme.getArchived() ))
        .body("categoryId", is(updateStudyProgramme.getCategoryId().intValue()));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/studyProgrammes/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudyProgramme() {
    StudyProgramme studyProgramme = new StudyProgramme(null, organizationId, "TST", "create type", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studyProgramme)
      .post("/students/studyProgrammes");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/studyProgrammes/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/studyProgrammes/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/studyProgrammes/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/studyProgrammes/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/studyProgrammes/{ID}", id)
      .then()
      .statusCode(404);
  }
}
