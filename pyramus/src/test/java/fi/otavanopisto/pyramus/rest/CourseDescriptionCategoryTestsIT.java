package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.CourseDescriptionCategory;

public class CourseDescriptionCategoryTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseDescriptionCategories() {
    CourseDescriptionCategory category = new CourseDescriptionCategory("New Category", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
    
    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(category.getName()))
      .body("archived", is( category.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/courses/descriptionCategories/{CATEGORYID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListCourseDescriptionCategories() {
    given().headers(getAuthHeaders())
      .get("/courses/descriptionCategories")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Basic" ))
      .body("archived[0]", is( false ))
      .body("id[1]", is(2) )
      .body("name[1]", is("Special" ))
      .body("archived[1]", is( false ));
  }
  
  @Test
  public void testFindCourseDescriptionCategory() {
    given().headers(getAuthHeaders())
      .get("/courses/descriptionCategories/1")
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Basic"))
      .body("archived", is( false ));
    
    given().headers(getAuthHeaders())
      .get("/courses/descriptionCategories/123")
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .get("/courses/descriptionCategories/abc")
      .then()
      .statusCode(404);
  }
  
  @Test
  public void testUpdateCourseDescriptionCategory() {
    CourseDescriptionCategory category = new CourseDescriptionCategory("Not Updated", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    try {
      response.then()
        .body("id", not(is((Long) null)))
        .body("name", is(category.getName()))
        .body("archived", is( category.getArchived() ));
      
      CourseDescriptionCategory updateCategory = new CourseDescriptionCategory(id, "Updated", Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateCategory)
        .put("/courses/descriptionCategories/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateCategory.getId().intValue()))
        .body("name", is(updateCategory.getName()))
        .body("archived", is( updateCategory.getArchived() ));  

    } finally {
      given().headers(getAuthHeaders())
        .delete("/courses/descriptionCategories/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteCourseDescriptionCategory() {
    CourseDescriptionCategory category = new CourseDescriptionCategory("To be deleted", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/courses/descriptionCategories/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/descriptionCategories/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/descriptionCategories/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/courses/descriptionCategories/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/descriptionCategories/{ID}", id)
      .then()
      .statusCode(404);
  }
}
