package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.Test;

import com.jayway.restassured.response.Response;

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
      .body("startDate", is( academicTerm.getStartDate().toString() ))
      .body("endDate", is( academicTerm.getEndDate().toString() ))
      .body("archived", is( academicTerm.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/calendar/academicTerms/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListAcademicTerms() {
    OffsetDateTime start1 = getDate(2014, 8, 1);
    OffsetDateTime end1 = getDate(2014, 12, 23);
    OffsetDateTime start2 = getDate(2015, 1, 4);
    OffsetDateTime end2 = getDate(2015, 5, 30);

    given().headers(getAuthHeaders())
      .get("/calendar/academicTerms")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1))
      .body("name[0]", is("fall"))
      .body("startDate[0]", is(start1.toString()))
      .body("endDate[0]", is(end1.toString()))
      .body("archived[0]", is(false))
      .body("id[1]", is(2))
      .body("name[1]", is("spring"))
      .body("startDate[1]", is(start2.toString()))
      .body("endDate[1]", is(end2.toString()))
      .body("archived[1]", is(false));
  }

  @Test
  public void testFindAcadmicTerm() {
    OffsetDateTime start = getDate(2014, 8, 1);
    OffsetDateTime end = getDate(2014, 12, 23);

    given().headers(getAuthHeaders()).get("/calendar/academicTerms/1").then().statusCode(200).body("id", is(1))
        .body("name", is("fall")).body("startDate", is(start.toString())).body("endDate", is(end.toString())).body("archived", is(false));
  }

  @Test
  public void testUpdateAcademicTerm() {
    AcademicTerm academicTerm = new AcademicTerm(null, "not updated", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);

    try {
      response.then().body("id", not(is((Long) null))).body("name", is(academicTerm.getName())).body("startDate", is(academicTerm.getStartDate().toString()))
          .body("endDate", is(academicTerm.getEndDate().toString())).body("archived", is(academicTerm.getArchived()));

      AcademicTerm updateAcademicTerm = new AcademicTerm(id, "updated", getDate(2010, 03, 04), getDate(2010, 07, 13), Boolean.FALSE);

      given().headers(getAuthHeaders()).contentType("application/json").body(updateAcademicTerm)
          .put("/calendar/academicTerms/{ID}", id).then().statusCode(200).body("id", is(updateAcademicTerm.getId().intValue()))
          .body("name", is(updateAcademicTerm.getName())).body("startDate", is(updateAcademicTerm.getStartDate().toString()))
          .body("endDate", is(updateAcademicTerm.getEndDate().toString())).body("archived", is(updateAcademicTerm.getArchived()));

    } finally {
      given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id).then().statusCode(204);
    }
  }

  @Test
  public void testDeleteAcademicTerm() {
    AcademicTerm academicTerm = new AcademicTerm(null, "to be deleted", getDate(2010, 02, 03), getDate(2010, 06, 12), Boolean.FALSE);

    Response response = given().headers(getAuthHeaders()).contentType("application/json").body(academicTerm)
        .post("/calendar/academicTerms");

    Long id = new Long(response.body().jsonPath().getInt("id"));
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

    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);

    given().headers(getAuthHeaders()).get("/calendar/academicTerms/{ID}/courses", id).then().statusCode(200)
        .body("id.size()", is(1)).body("id[0]", is(1000));

    given().headers(getAuthHeaders()).delete("/calendar/academicTerms/{ID}?permanent=true", id).then().statusCode(204);
  }
}
