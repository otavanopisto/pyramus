package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseDescriptionCategory;
import io.restassured.response.Response;

public class CourseDescriptionCategoryPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseDescriptionCategories(Role role) throws NoSuchFieldException{
    CourseDescriptionCategory category = new CourseDescriptionCategory("New Category", Boolean.FALSE);

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
    
    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSEDESCRIPTIONCATEGORY, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
        .delete("/courses/descriptionCategories/{CATEGORYID}?permanent=true", id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourseDescriptionCategories(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/descriptionCategories");
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSEDESCRIPTIONCATEGORIES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindCourseDescriptionCategory(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/descriptionCategories/1");
    assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSEDESCRIPTIONCATEGORY, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseDescriptionCategory(Role role) throws NoSuchFieldException {
    CourseDescriptionCategory category = new CourseDescriptionCategory("Not Updated", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
     
    Long id = response.body().jsonPath().getLong("id");
    
    try {
      CourseDescriptionCategory updateCategory = new CourseDescriptionCategory(id, "Updated", Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateCategory)
        .put("/courses/descriptionCategories/{ID}", id);
      assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSEDESCRIPTIONCATEGORY, 200);

    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/courses/descriptionCategories/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourseDescriptionCategory(Role role) throws NoSuchFieldException {
    CourseDescriptionCategory category = new CourseDescriptionCategory("To be deleted", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(category)
      .post("/courses/descriptionCategories");
     
    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/courses/descriptionCategories/{ID}", id);
    
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSEDESCRIPTIONCATEGORY, 204);
    
    given().headers(getAdminAuthHeaders())
      .delete("/courses/descriptionCategories/{ID}?permanent=true", id);
  }
}
