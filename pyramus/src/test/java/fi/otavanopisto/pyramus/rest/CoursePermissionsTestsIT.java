package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.model.Course;
import fi.otavanopisto.pyramus.rest.model.CourseLength;
import fi.otavanopisto.pyramus.rest.model.CourseModule;
import fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit;
import fi.otavanopisto.pyramus.rest.model.Subject;
import io.restassured.response.Response;

public class CoursePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CoursePermissions coursePermissions = new CoursePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourse(Role role) throws NoSuchFieldException {
    Course course = createCourse("Create test", OffsetDateTime.now(), OffsetDateTime.now(),
        "Course for testing course creation", Boolean.FALSE, 111, 222l,
        OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, OffsetDateTime.now(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, null, 1L);

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSE, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        deleteCourse(id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateCourseTags(Role role) throws NoSuchFieldException {
    Course course = createCourse("Create test", OffsetDateTime.now(), OffsetDateTime.now(),
        "Course for testing course creation", Boolean.FALSE, 111, 222l,
        OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, OffsetDateTime.now(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, Arrays.asList("tag1", "tag2", "tag3"), 1L);

    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    assertOk(role, response, coursePermissions, CoursePermissions.CREATE_COURSE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        deleteCourse(id);
      }
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsGetCourse(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courses/1001");
    assertOk(role, response, coursePermissions, CoursePermissions.FIND_COURSE, 200);
  }
 
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListCourses(Role role) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/courses/courses/");
    assertOk(role, response, coursePermissions, CoursePermissions.LIST_COURSES, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourse(Role role) throws NoSuchFieldException {
    Course course = createCourse("Update test", OffsetDateTime.now(), OffsetDateTime.now(),
        "Course for testing course updating", Boolean.FALSE, 111, 222l,
        OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, OffsetDateTime.now(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, null, 1L);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");

    course.setId(response.body().jsonPath().getLong("id"));
    course.setName("Updated name");
    
    course.setDescription("Updated description");
    firstCourseModule(course).setId(response.body().jsonPath().getLong("courseModules[0].id"));
    firstCourseModule(course).setCourseNumber(999);
    firstCourseModule(course).getCourseLength().setUnits(888d);
    course.setMaxParticipantCount(1234l);
    
    Response updateResponse = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId());
    assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSE, 200);
    
    deleteCourse(course.getId());
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateCourseTags(Role role) throws NoSuchFieldException {
    Course course = createCourse("Update test", OffsetDateTime.now(), OffsetDateTime.now(),
        "Course for testing course updating", Boolean.FALSE, 111, 222l,
        OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, OffsetDateTime.now(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, Arrays.asList(
            "tag1", "tag2", "tag3"), 1L);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
      
    course.setId(response.body().jsonPath().getLong("id"));
    course.setName("Updated name");
    course.setDescription("Updated description");
    firstCourseModule(course).setId(response.body().jsonPath().getLong("courseModules[0].id"));
    firstCourseModule(course).setCourseNumber(999);
    firstCourseModule(course).getCourseLength().setUnits(888d);
    course.setMaxParticipantCount(1234l);
    course.setTags(Arrays.asList("tag1", "tag3", "tag4", "tag5"));
    
    Response updateResponse = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(course)
      .put("/courses/courses/{ID}", course.getId());
    assertOk(role, updateResponse, coursePermissions, CoursePermissions.UPDATE_COURSE, 200);
    
    deleteCourse(course.getId());
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteCourse(Role role) throws NoSuchFieldException {
    Course course = createCourse("Update test", OffsetDateTime.now(), OffsetDateTime.now(),
        "Course for testing course updating", Boolean.FALSE, 111, 222l,
        OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), "Extension", 333d, 444d, 468d, 555d, 666d,
        777d, OffsetDateTime.now(), 1l, 1l, 1l, null, 777d, 1l, 1l, 1l, null, null, null, 1L);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(course)
      .post("/courses/courses/");
    
    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/courses/courses/{ID}", id);
    assertOk(role, deleteResponse, coursePermissions, CoursePermissions.DELETE_COURSE, 204);

    deleteCourse(id);
  }

  private void deleteCourse(Long id) {
    given().headers(getAdminAuthHeaders())
      .delete("/courses/courses/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  private CourseModule createCourseModule(Long courseModuleId, long subjectId, int courseNumber, double courseLengthUnits, long courseLengthUnitId) {
    EducationalTimeUnit courseLengthUnit = new EducationalTimeUnit(courseLengthUnitId, null, null, null, false);
    CourseLength courseLength = new CourseLength(null, null, courseLengthUnits, courseLengthUnit);
    Subject subject = new Subject(subjectId, null, null, null, false);
    return new CourseModule(courseModuleId, subject, courseNumber, courseLength);
  }

  private Course createCourse(String name, OffsetDateTime created, OffsetDateTime lastModified, String description, Boolean archived, Integer courseNumber, 
      Long maxParticipantCount, OffsetDateTime beginDate, OffsetDateTime endDate, OffsetDateTime signupStart, OffsetDateTime signupEnd, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingHours, Double distanceTeachingDays, Double assessingHours, Double planningHours, OffsetDateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Set<Long> curriculumIds, Double length, Long lengthUnitId, Long moduleId, Long stateId, Long typeId, 
      Map<String, String> variables, List<String> tags, Long organizationId) {
    Set<CourseModule> courseModules = new HashSet<>(Arrays.asList(createCourseModule(null, subjectId, courseNumber, length, lengthUnitId)));
    return new Course(null, name, created, lastModified, description, archived, maxParticipantCount, beginDate, endDate, signupStart, signupEnd, 
        nameExtension, localTeachingDays, teachingHours, distanceTeachingHours, distanceTeachingDays, assessingHours, planningHours, enrolmentTimeEnd, 
        creatorId, lastModifierId, curriculumIds, moduleId, stateId, typeId, variables, tags, organizationId, false, null, null, courseModules);
  }
  
  private CourseModule firstCourseModule(Course course) {
    return course.getCourseModules().iterator().next();
  }
}
