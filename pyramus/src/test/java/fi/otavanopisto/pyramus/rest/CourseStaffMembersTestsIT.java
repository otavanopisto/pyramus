package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.CourseStaffMember;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum;

public class CourseStaffMembersTestsIT extends AbstractRESTServiceTest {
  
  private static final long COURSE_ID = 1000;

  @Test
  public void testCreateCourseStaffMember() {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);
    
    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("role", is(CourseStaffMemberRoleEnum.COURSE_TEACHER.name()))
      .body("staffMemberId", is(entity.getStaffMemberId().intValue()))
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
      .body("id.size()", is(4))
      .body("id[0]", is(1))
      .body("role[0]", is(CourseStaffMemberRoleEnum.COURSE_TEACHER.name()))
      .body("courseId[0]", is((int) COURSE_ID)) 
      .body("staffMemberId[0]", is(1))    
      .body("id[1]", is(2))
      .body("role[1]", is(CourseStaffMemberRoleEnum.COURSE_TUTOR.name()))
      .body("courseId[1]", is((int) COURSE_ID)) 
      .body("staffMemberId[1]", is(2));
  }
  
  @Test
  public void testFindCourseStaffMember() {
    given().headers(getAuthHeaders())
    .get("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, 1l)
    .then()
    .statusCode(200)
      .body("id", is(1))
      .body("role", is(CourseStaffMemberRoleEnum.COURSE_TEACHER.name()))
      .body("courseId", is((int) COURSE_ID)) 
      .body("staffMemberId", is(1));   
  }
  
  @Test
  public void testUpdateCourseStaffMemberRole() {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    Long id = null;
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("role", is(CourseStaffMemberRoleEnum.COURSE_TEACHER.name()))
      .body("courseId", is((int) COURSE_ID)) 
      .body("staffMemberId", is(1));   
    
    try {
      id = response.body().jsonPath().getLong("id");
      
      CourseStaffMember updateEntity = new CourseStaffMember(id, null, 1l, CourseStaffMemberRoleEnum.COURSE_TUTOR);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, updateEntity.getId())
        .then()
        .statusCode(200)
        .body("role", is(CourseStaffMemberRoleEnum.COURSE_TUTOR.name()));


    } finally {
      given().headers(getAuthHeaders())
        .delete("/courses/courses/{COURSEID}/staffMembers/{ID}", COURSE_ID, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteCourseStaffMember() {
    CourseStaffMember entity = new CourseStaffMember(null, COURSE_ID, 1l, CourseStaffMemberRoleEnum.COURSE_TEACHER);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/courses/courses/{COURSEID}/staffMembers", COURSE_ID);

    response.then()
      .statusCode(200)
      .body("id", not(is((Long) null)))
      .body("role", is(CourseStaffMemberRoleEnum.COURSE_TEACHER.name()))
      .body("courseId", is((int) COURSE_ID)) 
      .body("staffMemberId", is(1));  
    
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
