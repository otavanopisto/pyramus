package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseDescriptionCategory;

@RunWith(Parameterized.class)
public class CourseDescriptionCategoryPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CourseDescriptionCategoryPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateCourseDescriptionCategories() throws NoSuchFieldException{
    CourseDescriptionCategory category = new CourseDescriptionCategory("New Category", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSEDESCRIPTIONCATEGORY, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/descriptionCategories/{CATEGORYID}?permanent=true", id);
      }
    }
  }
  
  @Test
  public void testPermissionsListCourseDescriptionCategories() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
      .get("/courses/descriptionCategories");
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSEDESCRIPTIONCATEGORIES, 200);
  }
  
  @Test
  public void testPermissionsFindCourseDescriptionCategory() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
      .get("/courses/descriptionCategories/1");
    assertOk(response, coursePermissions, CoursePermissions.FIND_COURSEDESCRIPTIONCATEGORY, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourseDescriptionCategory() throws NoSuchFieldException {
    CourseDescriptionCategory category = new CourseDescriptionCategory("Not Updated", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    try {
      CourseDescriptionCategory updateCategory = new CourseDescriptionCategory(id, "Updated", Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateCategory)
        .put("/courses/descriptionCategories/{ID}", id);
      assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSEDESCRIPTIONCATEGORY, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/descriptionCategories/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testPermissionsDeleteCourseDescriptionCategory() throws NoSuchFieldException {
    CourseDescriptionCategory category = new CourseDescriptionCategory("To be deleted", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
     
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/descriptionCategories/{ID}", id);
    
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSEDESCRIPTIONCATEGORY, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/descriptionCategories/{ID}?permanent=true", id);
  }
}
