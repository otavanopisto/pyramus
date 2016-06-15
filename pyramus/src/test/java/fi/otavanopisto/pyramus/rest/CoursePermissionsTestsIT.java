package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.Course;

@RunWith(Parameterized.class)
public class CoursePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public CoursePermissionsTestsIT(String role) {
      this.role = role;
  }
  @Test
  public void testPermissionsCreateCourse() throws NoSuchFieldException {
    Course course = createCourse("Create test", new DateTime(), new DateTime(),
        "Course for testing course creation", Boolean.FALSE, 111, 222l,
        new DateTime(), new DateTime(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, new DateTime(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, null);

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSE, 200);

    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        deleteCourse(id);
      }
    }
  }
  
  @Test
  public void testPermissionsCreateCourseTags() throws NoSuchFieldException {
    Course course = createCourse("Create test", new DateTime(), new DateTime(),
        "Course for testing course creation", Boolean.FALSE, 111, 222l,
        new DateTime(), new DateTime(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, new DateTime(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, Arrays.asList("tag1", "tag2", "tag3"));

    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    assertOk(response, coursePermissions, CoursePermissions.CREATE_COURSE, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        deleteCourse(id);
      }
    }
  }
  
  @Test
  public void testPermissionsGetCourse() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/1001");
    assertOk(response, coursePermissions, CoursePermissions.FIND_COURSE, 200);
  }
 
  @Test
  public void testPermissionsListCourses() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/courses/courses/");
    assertOk(response, coursePermissions, CoursePermissions.LIST_COURSES, 200);
  }
  
  @Test
  public void testPermissionsUpdateCourse() throws NoSuchFieldException {
    Course course = createCourse("Update test", new DateTime(), new DateTime(),
        "Course for testing course updating", Boolean.FALSE, 111, 222l,
        new DateTime(), new DateTime(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, new DateTime(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, null);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");

    course.setId(new Long(response.body().jsonPath().getInt("id")));
    course.setName("Updated name");
    course.setCourseNumber(999);
    course.setDescription("Updated description");
    course.setLength(888d);
    course.setMaxParticipantCount(1234l);
    
    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId());
    assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSE, 200);
    
    deleteCourse(course.getId());
  }
  
  @Test
  public void testPermissionsUpdateCourseTags() throws NoSuchFieldException {
    Course course = createCourse("Update test", new DateTime(), new DateTime(),
        "Course for testing course updating", Boolean.FALSE, 111, 222l,
        new DateTime(), new DateTime(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, new DateTime(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, Arrays.asList(
            "tag1", "tag2", "tag3"));

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
      
    course.setId(new Long(response.body().jsonPath().getInt("id")));
    course.setName("Updated name");
    course.setCourseNumber(999);
    course.setDescription("Updated description");
    course.setLength(888d);
    course.setMaxParticipantCount(1234l);
    course.setTags(Arrays.asList("tag1", "tag3", "tag4", "tag5"));
    
    Response updateResponse = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId());
    assertOk(updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSE, 200);
    
    deleteCourse(course.getId());
  }
  
  @Test
  public void testPermissionsDeleteCourse() throws NoSuchFieldException {
    Course course = createCourse("Update test", new DateTime(), new DateTime(),
        "Course for testing course updating", Boolean.FALSE, 111, 222l,
        new DateTime(), new DateTime(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, new DateTime(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, null);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/courses/courses/{ID}", id);
    assertOk(deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSE, 204);

    deleteCourse(id);
  }

  private void deleteCourse(Long id) {
    given().headers(getAdminAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  private Course createCourse(String name, DateTime created, DateTime lastModified, String description, Boolean archived, Integer courseNumber, 
      Long maxParticipantCount, DateTime beginDate, DateTime endDate, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingHours, Double distanceTeachingDays, Double assessingHours, Double planningHours, DateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Long curriculumId, Double length, Long lengthUnitId, Long moduleId, Long stateId, Long typeId, 
      Map<String, String> variables, List<String> tags) {
    return new Course(null, name, created, lastModified, description, archived, courseNumber, maxParticipantCount, beginDate, endDate, 
        nameExtension, localTeachingDays, teachingHours, distanceTeachingHours, distanceTeachingDays, assessingHours, planningHours, enrolmentTimeEnd, 
        creatorId, lastModifierId, subjectId, curriculumId, length, lengthUnitId, moduleId, stateId, typeId, variables, tags);
  }
  
}
