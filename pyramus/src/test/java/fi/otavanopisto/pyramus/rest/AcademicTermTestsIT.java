package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.AcademicTerm;

public class AcademicTermTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateAcadmicTerm() {
    OffsetDateTime start = getDate(2010, 02, 03);
    OffsetDateTime end = getDate(2010, 06, 12);
    
    AcademicTerm academicTerm = new AcademicTerm(null, "create test", start, end, Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(academicTerm)
      .post("/calendar/academicTerms");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(academicTerm.getName()))
      .body("startDate", is( getDateString(academicTerm.getStartDate().getYear(), academicTerm.getStartDate().getMonth().getValue(), academicTerm.getStartDate().getDayOfMonth() )))
      .body("endDate", is( getDateString(academicTerm.getEndDate().getYear(), academicTerm.getEndDate().getMonth().getValue(), academicTerm.getEndDate().getDayOfMonth() )))
      .body("archived", is( academicTerm.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/calendar/academicTerms/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListAcademicTerms() {
    String start1 = getDateString(2014, 8, 1);
    String end1 = getDateString(2014, 12, 23);
    String start2 = getDateString(2015, 1, 4);
    String end2 = getDateString(2015, 5, 30);

    given().headers(getAuthHeaders())
      .get("/calendar/academicTerms")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1))
      .body("name[0]", is("fall"))
      .body("startDate[0]", is(start1))
      .body("endDate[0]", is(end1))
      .body("archived[0]", is(false))
      .body("id[1]", is(2))
      .body("name[1]", is("spring"))
      .body("startDate[1]", is(start2))
      .body("endDate[1]", is(end2))
      .body("archived[1]", is(false));
  }

  @Test
  public void testFindAcadmicTerm() {
    String start = getDateString(2014, 8, 1);
    String end = getDateString(2014, 12, 23);

    given().headers(getAuthHeaders()).get("/calendar/academicTerms/1").then().statusCode(200).body("id", is(1))
        .body("name", is("fall")).body("startDate", is(start)).body("endDate", is(end)).body("archived", is(false));
  }

  @Test
  public void testUpdateAcademicTerm() {
    AcademicTerm academicTerm = new AcademicTerm(null, "not updated", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);

    try {
      response.then()
        .body("id", not(is((Long) null)))
        .body("name", is(academicTerm.getName()))
        .body("startDate", is(getDateString(academicTerm.getStartDate().getYear(), academicTerm.getStartDate().getMonth().getValue(), academicTerm.getStartDate().getDayOfMonth()) ))
        .body("endDate", is(getDateString(academicTerm.getEndDate().getYear(), academicTerm.getEndDate().getMonth().getValue(), academicTerm.getEndDate().getDayOfMonth()) ))
        .body("archived", is(academicTerm.getArchived()));

      AcademicTerm updateAcademicTerm = new AcademicTerm(id, "updated", getDate(2010, 03, 04), getDate(2010, 07, 13), Boolean.FALSE);

      given().headers(getAuthHeaders()).contentType("application/json")
        .body(updateAcademicTerm)
        .put("/calendar/academicTerms/{ID}", id).then().statusCode(200)
        .body("id", is(updateAcademicTerm.getId().intValue()))
        .body("name", is(updateAcademicTerm.getName()))
        .body("startDate", is(getDateString(updateAcademicTerm.getStartDate().getYear(), updateAcademicTerm.getStartDate().getMonth().getValue(), updateAcademicTerm.getStartDate().getDayOfMonth()) ))
        .body("endDate", is(getDateString(updateAcademicTerm.getEndDate().getYear(), updateAcademicTerm.getEndDate().getMonth().getValue(), updateAcademicTerm.getEndDate().getDayOfMonth()) ))
        .body("archived", is(updateAcademicTerm.getArchived()));

    } finally {
      given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id).then().statusCode(204);
    }
  }

  @Test
  public void testDeleteAcademicTerm() {
    AcademicTerm academicTerm = new AcademicTerm(null, "to be deleted", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);

    given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}", id).then().statusCode(200);

    given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}", id).then().statusCode(204);

    given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}", id).then().statusCode(404);

    given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id).then().statusCode(204);

    given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}", id).then().statusCode(404);
  }

  @Test
  public void testCoursesByTerm() {
    AcademicTerm academicTerm = new AcademicTerm(null, "2010 spring", getDate(2010, 01, 01), getDate(2010, 06, 1), Boolean.FALSE);

    Response response = given().headers(getAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);

    given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}/courses", id).then().statusCode(200)
        .body("id.size()", is(1)).body("id[0]", is(1000));

    given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id).then().statusCode(204);
  }
}
