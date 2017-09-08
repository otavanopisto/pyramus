package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.School;

public class SchoolTestsIT extends AbstractRESTServiceTest {

 //FIXME: find out why i break all tests that run after me
  @Test
  public void testCreateSchool() {
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    School school = new School(null, "TST", "to be created", Arrays.asList("tag1", "tag2"), 1l, "additional info", Boolean.FALSE, variables);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(school.getName()))
      .body("code", is(school.getCode()))
      .body("additionalContactInfo", is(school.getAdditionalContactInfo()))
      .body("fieldId", is(school.getFieldId().intValue()))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("variables", allOf(hasEntry("TV1", "text"), hasEntry("TV2", "123")))
      .body("archived", is( school.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/schools/schools/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListSchools() {
    given().headers(getAuthHeaders())
      .get("/schools/schools")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("School #1"))
      .body("code[0]", is("TST1"))
      .body("fieldId[0]", is(1))
      .body("id[1]", is(2) )
      .body("name[1]", is("School #2"))
      .body("code[1]", is("TST2"))
      .body("fieldId[1]", is(1));
  }
  
  @Test
  public void testFindSchool() {
    given().headers(getAuthHeaders())
      .get("/schools/schools/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("School #1"))
      .body("code", is("TST1"));
  }
  
  @Test
  public void testUpdateSchool() {
    Map<String, String> variables = new HashMap<>();
    variables.put("TV1", "text");
    variables.put("TV2", "123");
    
    School school = new School(null, "TST", "not updated", Arrays.asList("tag1", "tag2"), 1l, "not updated info", Boolean.FALSE, variables);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(school.getName()))
      .body("code", is(school.getCode()))
      .body("additionalContactInfo", is(school.getAdditionalContactInfo()))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("variables", allOf(hasEntry("TV1", "text"), hasEntry("TV2", "123")))
      .body("archived", is( school.getArchived() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Map<String, String> updateVariables = new HashMap<>();
      updateVariables.put("TV2", "234");
      updateVariables.put("TV3", "1");

      School updateSchool = new School(id, "UPD", "updated", Arrays.asList("tag2", "tag3"), 2l, "updated info", Boolean.FALSE, updateVariables);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateSchool)
        .put("/schools/schools/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateSchool.getId().intValue() ))
        .body("name", is(updateSchool.getName()))
        .body("code", is(updateSchool.getCode()))
        .body("additionalContactInfo", is(updateSchool.getAdditionalContactInfo()))
        .body("tags.size()", is(2))
        .body("tags", allOf(hasItem("tag2"), hasItem("tag3") ))
        .body("variables", allOf(hasEntry("TV2", "234"), hasEntry("TV3", "1"), not(hasKey("TV1"))))
        .body("archived", is( updateSchool.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/schools/schools/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteSchool() {
    School school = new School(null, "TST", "to be deleted", Arrays.asList("tag1", "tag2"), 1l, "additional", Boolean.FALSE, null);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(school)
      .post("/schools/schools");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(school.getName()))
      .body("code", is(school.getCode()))
      .body("additionalContactInfo", is(school.getAdditionalContactInfo()))
      .body("fieldId", is(school.getFieldId().intValue()))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is( school.getArchived() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/schools/schools/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/schools/schools/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/schools/schools/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/schools/schools/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/schools/schools/{ID}", id)
      .then()
      .statusCode(404);
  }
  
}
