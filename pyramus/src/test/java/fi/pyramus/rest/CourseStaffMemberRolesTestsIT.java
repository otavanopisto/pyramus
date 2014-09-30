package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.CourseStaffMemberRole;

public class CourseStaffMemberRolesTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateCourseStaffMemberRole() {
    CourseStaffMemberRole entity = new CourseStaffMemberRole(null, "created");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/staffMemberRoles");
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(entity.getName()));
    
    int id = response.body().jsonPath().getInt("id");

    given().headers(getAuthHeaders())
      .delete("/courses/staffMemberRoles/{ID}", id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListCourseStaffMemberRoles() {
    given().headers(getAuthHeaders())
      .get("/courses/staffMemberRoles")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1))
      .body("name[0]", is("Teacher"))
      .body("id[1]", is(2))
      .body("name[1]", is("Tutor"));
  }
  
  @Test
  public void testFindCourseStaffMemberRole() {
    given().headers(getAuthHeaders())
    .get("/courses/staffMemberRoles/{ID}", 1l)
    .then()
    .statusCode(200)
      .body("id", is(1))
      .body("name", is("Teacher"));
  }
  
  @Test
  public void testUpdateCourseStaffMemberRole() {
    CourseStaffMemberRole entity = new CourseStaffMemberRole(null, "not updated");
    Long id = null;
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/staffMemberRoles");

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(entity.getName()));
    
    try {
      id = response.body().jsonPath().getLong("id");
      
      CourseStaffMemberRole updateEntity = new CourseStaffMemberRole(id, "updated");
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/staffMemberRoles/{ID}", updateEntity.getId())
        .then()
        .statusCode(200)
        .body("name", is(updateEntity.getName()));


    } finally {
      given().headers(getAuthHeaders())
        .delete("/courses/staffMemberRoles/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteCourseStaffMemberRole() {
    CourseStaffMemberRole entity = new CourseStaffMemberRole(null, "to be deleted");
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/staffMemberRoles");

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("name", is(entity.getName()));
    
    Long id = response.body().jsonPath().getLong("id");

    given().headers(getAuthHeaders()).get("/courses/staffMemberRoles/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/staffMemberRoles/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/staffMemberRoles/{ID}", id)
      .then()
      .statusCode(404);
  }
}
