package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Grade;

public class GradeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateGrade() {
    Grade grade = new Grade(null, "create grade", "grade for testing grading creation", 1l, Boolean.TRUE, "qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(grade.getName()))
      .body("description", is(grade.getDescription()))
      .body("gpa", is(grade.getGpa().floatValue()))
      .body("qualification", is(grade.getQualification()))
      .body("passingGrade", is(grade.getPassingGrade()))
      .body("gradingScaleId", is(grade.getGradingScaleId().intValue()))
      .body("archived", is( grade.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListGrades() {given().headers(getAuthHeaders())
      .get("/common/gradingScales/1/grades")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("gpa[0]", is(10f) )
      .body("name[0]", is("test grade #1"))
      .body("description[0]", is("grade for testing #1"))
      .body("archived[0]", is( false ))
      .body("passingGrade[0]", is( false ))
      .body("qualification[0]", is( "qualification #1" ))
      .body("gradingScaleId[0]", is( 1 ))
      .body("id[1]", is(2) )
      .body("gpa[1]", is(20f) )
      .body("name[1]", is("test grade #2"))
      .body("description[1]", is("grade for testing #2"))
      .body("archived[1]", is( false ))
      .body("passingGrade[1]", is( true ))
      .body("qualification[1]", is( "qualification #2" ))
      .body("gradingScaleId[1]", is( 1 ));
  }
  
  @Test
  public void testFindGradingScale() {
    given().headers(getAuthHeaders())
      .get("/common/gradingScales/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("test scale #1" ))
      .body("description", is("grading scale for testing #1"))
      .body("archived", is( false ));
  }
  
  @Test
  public void testUpdateGrade() {
    Grade grade = new Grade(null, "not updated", "not updated grade", 1l, Boolean.FALSE, "not updated qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(grade.getName()))
      .body("description", is(grade.getDescription()))
      .body("gpa", is(grade.getGpa().floatValue()))
      .body("qualification", is(grade.getQualification()))
      .body("passingGrade", is(grade.getPassingGrade()))
      .body("gradingScaleId", is(grade.getGradingScaleId().intValue()))
      .body("archived", is( grade.getArchived() ));
    
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    try {
      Grade updateGrade = new Grade(id, "updated", "updated grade", 1l, Boolean.TRUE, "updated qualification", 10d, Boolean.FALSE);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateGrade)
        .put("/common/gradingScales/{SCALEID}/grades/{ID}", updateGrade.getGradingScaleId(), id)
        .then()
        .statusCode(200)
        .body("id", is(updateGrade.getId().intValue()))
        .body("name", is(updateGrade.getName()))
        .body("description", is(updateGrade.getDescription()))
        .body("gpa", is(updateGrade.getGpa().floatValue()))
        .body("qualification", is(updateGrade.getQualification()))
        .body("passingGrade", is(updateGrade.getPassingGrade()))
        .body("gradingScaleId", is(updateGrade.getGradingScaleId().intValue()))
        .body("archived", is( updateGrade.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteGrade() {
    Grade grade = new Grade(null, "to be deleted", "grade to be deleted", 1l, Boolean.TRUE, "qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(grade.getName()))
      .body("description", is(grade.getDescription()))
      .body("gpa", is(grade.getGpa().floatValue()))
      .body("qualification", is(grade.getQualification()))
      .body("passingGrade", is(grade.getPassingGrade()))
      .body("gradingScaleId", is(grade.getGradingScaleId().intValue()))
      .body("archived", is( grade.getArchived() ));
    
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/common/gradingScales/{SCALEID}/grades/{ID}", grade.getGradingScaleId(), id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/common/gradingScales/{SCALEID}/grades/{ID}", grade.getGradingScaleId(), id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/gradingScales/{SCALEID}/grades/{ID}", grade.getGradingScaleId(), id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/common/gradingScales/{SCALEID}/grades/{ID}", grade.getGradingScaleId(), id)
      .then()
      .statusCode(404);
  }
}
