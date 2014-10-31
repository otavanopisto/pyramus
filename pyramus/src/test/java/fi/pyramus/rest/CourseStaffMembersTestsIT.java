package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.CourseStaffMember;

public class CourseStaffMembersTestsIT extends AbstractRESTServiceTest {
  
  private static final long COURSE_ID = 1000;

  @Test
  public void testCreateCourseStaffMember() {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, 1l);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("roleId", is(entity.getRoleId().intValue()))
      .body("userId", is(entity.getStaffMemberId().intValue()))
      .body("courseId", is(entity.getCourseId().intValue()));
    
    int id = response.body().jsonPath().getInt("id");

    given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id)
      .then()
      .statusCode(204);
  }

  @Test
  public void testListCourseStaffMembers() {
    given().headers(getAuthHeaders())
      .get("/courses/courses/{COURSEID}/staffMembers", COURSE_ID)
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1))
      .body("roleId[0]", is(1))
      .body("courseId[0]", is((int) COURSE_ID)) 
      .body("userId[0]", is(1))    
      .body("id[1]", is(2))
      .body("roleId[1]", is(2))
      .body("courseId[1]", is((int) COURSE_ID)) 
      .body("userId[1]", is(2));
  }
  
  @Test
  public void testFindCourseStaffMember() {
    given().headers(getAuthHeaders())
    .get("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, 1l)
    .then()
    .statusCode(200)
      .body("id", is(1))
      .body("roleId", is(1))
      .body("courseId", is((int) COURSE_ID)) 
      .body("userId", is(1));   
  }
  
  @Test
  public void testUpdateCourseStaffMemberRole() {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, 1l);
    Long id = null;
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("roleId", is(1))
      .body("courseId", is((int) COURSE_ID)) 
      .body("userId", is(1));   
    
    try {
      id = response.body().jsonPath().getLong("id");
      
      CourseStaffMember updateEntity = new CourseStaffMember(id, null, 1l, 2l);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, updateEntity.getId())
        .then()
        .statusCode(200)
        .body("roleId", is(2));


    } finally {
      given().headers(getAuthHeaders())
        .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteCourseStaffMember() {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, 1l);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("roleId", is(1))
      .body("courseId", is((int) COURSE_ID)) 
      .body("userId", is(1));  
    
    Long id = response.body().jsonPath().getLong("id");

    given().headers(getAuthHeaders()).get("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id)
      .then()
      .statusCode(404);
  }
}
