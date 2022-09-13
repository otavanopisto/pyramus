package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryType;

public class StudentContactLogEntryTestsIT extends AbstractRESTServiceTest {
  
  private final static long TEST_STUDENT_ID = 3l;

  @Test
  public void testCreateStudentContactLogEntry() {
    StudentContactLogEntry studentContactLogEntry = new StudentContactLogEntry(null, "create text", null, "creator", getDate(2010, 3, 5), StudentContactLogEntryType.CHATLOG, null, Boolean.FALSE);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentContactLogEntry)
      .post("/students/students/{ID}/contactLogEntries", TEST_STUDENT_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("text", is(studentContactLogEntry.getText()))
      .body("entryDate", is(studentContactLogEntry.getEntryDate().toString()))
      .body("type", is(studentContactLogEntry.getType().toString()))
      .body("archived", is( studentContactLogEntry.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}?permanent=true", TEST_STUDENT_ID, id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListStudentContactLogEntries() {
    given().headers(getAuthHeaders())
      .get("/students/students/{ID}/contactLogEntries?resultsPerPage=10&page=0", TEST_STUDENT_ID)
      .then()
      .statusCode(200)
      .body("results.id.size()", is(2))
      .body("results.id[1]", is(1) )
      .body("results.text[1]", is("Test text #1"))
      .body("results.creatorName[1]", is("Tester #1"))
      .body("results.entryDate[1]", is(getDate(2010, 1, 1).toString()))
      .body("results.type[1]", is("LETTER"))
      .body("results.archived[1]", is( Boolean.FALSE ))
      .body("results.id[0]", is(2) )
      .body("results.text[0]", is("Test text #2"))
      .body("results.creatorName[0]", is("Tester #2"))
      .body("results.entryDate[0]", is(getDate(2011, 1, 1).toString()))
      .body("results.type[0]", is("PHONE"))
      .body("results.archived[0]", is( Boolean.FALSE ));
  }
  
  @Test
  public void testUpdateStudentContactLogEntry() {
    StudentContactLogEntry studentContactLogEntry = new StudentContactLogEntry(null, "not updated text", null, "not updated creater", getDate(2010, 3, 5), StudentContactLogEntryType.CHATLOG, null, Boolean.FALSE);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentContactLogEntry)
      .post("/students/students/{ID}/contactLogEntries", TEST_STUDENT_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("text", is(studentContactLogEntry.getText()))
      .body("entryDate", is(studentContactLogEntry.getEntryDate().toString()))
      .body("type", is(studentContactLogEntry.getType().toString()))
      .body("archived", is( studentContactLogEntry.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    try {
      StudentContactLogEntry updateContactLogEntry = new StudentContactLogEntry(id, "updated text", id, "updated creater", getDate(2013, 3, 5), StudentContactLogEntryType.FACE2FACE, null, Boolean.FALSE);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateContactLogEntry)
        .put("/students/students/{STUDENTID}/contactLogEntries/{ID}", TEST_STUDENT_ID, id)
        .then()
        .statusCode(200)
        .body("id", is(updateContactLogEntry.getId().intValue() ))
        .body("text", is(updateContactLogEntry.getText()))
        .body("entryDate", is(updateContactLogEntry.getEntryDate().toString()))
        .body("type", is(updateContactLogEntry.getType().toString()))
        .body("archived", is( updateContactLogEntry.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}?permanent=true", TEST_STUDENT_ID, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentContactLogEntry() {
    StudentContactLogEntry studentContactLogEntry = new StudentContactLogEntry(null, "create text", null, "creator name", getDate(2010, 3, 5), StudentContactLogEntryType.CHATLOG, null, Boolean.FALSE);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentContactLogEntry)
      .post("/students/students/{ID}/contactLogEntries", TEST_STUDENT_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("text", is(studentContactLogEntry.getText()))
      .body("entryDate", is(studentContactLogEntry.getEntryDate().toString()))
      .body("type", is(studentContactLogEntry.getType().toString()))
      .body("archived", is( studentContactLogEntry.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}", TEST_STUDENT_ID, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}?permanent=true", TEST_STUDENT_ID, id)
      .then()
      .statusCode(204);
  }
}
