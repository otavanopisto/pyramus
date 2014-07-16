package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.EducationSubtype;

public class EducationSubtypeTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateEducationSubtype() {
    EducationSubtype educationSubtype = new EducationSubtype(null, "create sub type", "TST", 1l, Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationSubtype.getName()))
      .body("code", is(educationSubtype.getCode()))
      .body("educationTypeId", is( educationSubtype.getEducationTypeId().intValue() ))
      .body("archived", is( educationSubtype.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given()
      .delete("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}?permanent=true", educationSubtype.getEducationTypeId(), id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListEducationSubtypes() {
    given()
      .get("/common/educationTypes/{EDUCATIONTYPE}/subtypes", 1)
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Test Subtype #1" ))
      .body("code[0]", is("TST1"))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Test Subtype #2" ))
      .body("code[1]", is("TST2"))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindEducationSubtype() {
    given()
      .get("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", 1, 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Test Subtype #1" ))
      .body("code", is("TST1"))
      .body("archived", is( false ));
  }
    
  @Test
  public void testUpdateEducationSubtype() {
    Long updateEducationTypeId = 1l;
    EducationSubtype educationSubtype = new EducationSubtype(null, "Not Updated", "NOT", 2l, Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(educationSubtype.getName()))
      .body("code", is(educationSubtype.getCode()))
      .body("educationSubtypeId", is(educationSubtype.getId()))
      .body("archived", is( educationSubtype.getArchived() ));
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      EducationSubtype updateSubtype = new EducationSubtype(id, "Updated", "UPD", updateEducationTypeId, Boolean.FALSE);

      given()
        .contentType("application/json")
        .body(updateSubtype)
        .put("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), updateSubtype.getId())
        .then()
        .statusCode(200)
        .body("id", is( updateSubtype.getId().intValue() ))
        .body("name", is(updateSubtype.getName()))
        .body("code", is(updateSubtype.getCode()))
        .body("educationTypeId", is(updateEducationTypeId.intValue()))
        .body("archived", is( updateSubtype.getArchived() ));

    } finally {
      given()
        .delete("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}?permanent=true", updateEducationTypeId, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteEducationSubtype() {
    EducationSubtype educationSubtype = new EducationSubtype(null, "create subtype", "TST", 1l, Boolean.FALSE);
    
    Response response = given()
      .contentType("application/json")
      .body(educationSubtype)
      .post("/common/educationTypes/{EDUCATIONTYPE}/subtypes", educationSubtype.getEducationTypeId());
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().get("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), id)
      .then()
      .statusCode(204);
    
    given().get("/common/educationTypes/{EDUCATIONTYPE}/subtypes/{ID}", educationSubtype.getEducationTypeId(), id)
      .then()
      .statusCode(404);
  }
}
