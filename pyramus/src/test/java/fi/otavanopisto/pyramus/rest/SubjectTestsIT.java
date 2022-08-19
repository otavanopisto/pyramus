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
  public void testListSubjectCourses() {
    String created1 = getDateString(2010, 1, 1);
    String modified1 = getDateString(2010, 1, 1);
    String beginDate1 = getDateString(2010, 2, 2);
    String endDate1 = getDateString(2010, 3, 3);
    String enrolmentTimeEnd1 = getDateString(2010, 1, 1);
    
    String created2 = getDateString(2011, 1, 1);
    String modified2 = getDateString(2011, 1, 1);
    String beginDate2 = getDateString(2011, 2, 2);
    String endDate2 = getDateString(2011, 3, 3);
    String enrolmentTimeEnd2 = getDateString(2011, 1, 1);

    given().headers(getAuthHeaders())
      .get("/common/subjects/{ID}/courses", 1)
      .then()
      .body("id.size()", is(2))
      .body("id[0]", is(1000) )
      .body("name[0]", is("Test Course #1" ))
      .body("created[0]", is( created1 ))
      .body("lastModified[0]", is( modified1 ))
      .body("beginDate[0]", is( beginDate1 ))
      .body("endDate[0]", is( endDate1 ))
      .body("enrolmentTimeEnd[0]", is( enrolmentTimeEnd1 ))
      .body("description[0]", is( "Course #1 for testing" ))
      .body("creatorId[0]", is( 1 ))
      .body("lastModifierId[0]", is( 1 ))
      .body("courseModules[0].size()", is( 1 ))
      .body("courseModules[0][0].courseNumber", is( 1 ))
      .body("courseModules[0][0].courseLength.units", is( 1.0f ))
      .body("courseModules[0][0].courseLength.unit.id", is( 1 ))
      .body("courseModules[0][0].subject.id", is( 1 ))
      .body("maxParticipantCount[0]", is( 100 ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(1001) )
      .body("name[1]", is("Test Course #2" ))
      .body("created[1]", is( created2 ))
      .body("lastModified[1]", is( modified2 ))
      .body("beginDate[1]", is( beginDate2 ))
      .body("endDate[1]", is( endDate2 ))
      .body("enrolmentTimeEnd[1]", is( enrolmentTimeEnd2 ))
      .body("description[1]", is( "Course #2 for testing" ))
      .body("creatorId[1]", is( 1 ))
      .body("lastModifierId[1]", is( 1 ))
      .body("courseModules[1].size()", is( 1 ))
      .body("courseModules[1][0].courseNumber", is( 2 ))
      .body("courseModules[1][0].courseLength.units", is( 1.0f ))
      .body("courseModules[1][0].courseLength.unit.id", is( 1 ))
      .body("courseModules[1][0].subject.id", is( 1 ))
      .body("maxParticipantCount[1]", is( 200 ))
      .body("archived[1]", is( false ));
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
