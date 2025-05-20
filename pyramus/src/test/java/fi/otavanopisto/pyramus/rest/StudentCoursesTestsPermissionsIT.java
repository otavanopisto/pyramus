package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;

public class StudentCoursesTestsPermissionsIT extends AbstractRESTPermissionsTestJUnit5 {

  private final static long TEST_STUDENT_ID = 3l;
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsStudentListCourses(Role role) throws NoSuchFieldException {
    if (roleIsAllowed(role, studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(role, given().headers(getAuthHeaders(role))
          .get("/students/students/{ID}/courses", TEST_STUDENT_ID), studentPermissions, StudentPermissions.LIST_COURSESTUDENTSBYSTUDENT, 403);
    }
    else {
      assertOk(role, given().headers(getAuthHeaders(role))
          .get("/students/students/{ID}/courses", TEST_STUDENT_ID), studentPermissions, StudentPermissions.LIST_COURSESTUDENTSBYSTUDENT, 200);
    }
  }
}
