package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Subject;

public class SubjectTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateSubject() {
    Subject subject = new Subject(null, "TST", "create subject", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(subject.getName()))
      .body("code", is(subject.getCode()))
      .body("educationTypeId", is(subject.getEducationTypeId().intValue()))
      .body("archived", is( subject.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/common/subjects/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListSubjects() {
    given().headers(getAuthHeaders())
      .get("/common/subjects")
      .then()
      .statusCode(200)
      .body("id.size()", is(4))
      .body("id[0]", is(1) )
      .body("name[0]", is("Test Subject" ))
      .body("code[0]", is("TEST"))
      .body("educationTypeId[0]", is(1))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Test Subject #2" ))
      .body("code[1]", is("TST2"))
      .body("educationTypeId[1]", is(2))
      .body("archived[1]", is( false ))
      .body("id[2]", is(3) )
      .body("name[2]", is("Test Subject #3" ))
      .body("code[2]", is("TST3"))
      .body("educationTypeId[2]", is(2))
      .body("archived[2]", is( false ));
  }
  
  @Test
  public void testFindSubject() {
    given().headers(getAuthHeaders())
      .get("/common/subjects/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Test Subject" ))
      .body("code", is("TEST"))
      .body("educationTypeId", is(1))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateSubject() {
    Subject subject = new Subject(null, "NUPD", "not updated", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(subject.getName()))
      .body("code", is(subject.getCode()))
      .body("educationTypeId", is(subject.getEducationTypeId().intValue()))
      .body("archived", is( subject.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Subject updateSubject = new Subject(id, "UPD", "updated", 2l, Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateSubject)
        .put("/common/subjects/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is( updateSubject.getId().intValue() ))
        .body("name", is(updateSubject.getName()))
        .body("code", is(updateSubject.getCode()))
        .body("educationTypeId", is(updateSubject.getEducationTypeId().intValue() ))
        .body("archived", is( updateSubject.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/common/subjects/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteSubject() {
    Subject subject = new Subject(null, "DEL", "to be deleted", 1l, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(subject)
      .post("/common/subjects");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/common/subjects/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/common/subjects/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/subjects/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/common/subjects/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/subjects/{ID}", id)
      .then()
      .statusCode(404);
  }
}
