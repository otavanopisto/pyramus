package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;

@RunWith(Parameterized.class)
public class StudentCoursesTestsPermissionsIT extends AbstractRESTPermissionsTest{

  private final static long TEST_STUDENT_ID = 3l;
  private StudentPermissions studentPermissions = new StudentPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public StudentCoursesTestsPermissionsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsStudentListCourses() throws NoSuchFieldException {
    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(given().headers(getAuthHeaders())
          .get("/students/students/{ID}/courses", TEST_STUDENT_ID), studentPermissions, StudentPermissions.LIST_COURSESTUDENTSBYSTUDENT, 403);
    }
    else {
      assertOk(given().headers(getAuthHeaders())
          .get("/students/students/{ID}/courses", TEST_STUDENT_ID), studentPermissions, StudentPermissions.LIST_COURSESTUDENTSBYSTUDENT, 200);
    }
  }
}
