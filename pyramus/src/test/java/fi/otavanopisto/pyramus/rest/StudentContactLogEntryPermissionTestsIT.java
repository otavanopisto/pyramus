package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.StudentContactLogEntryPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryType;

@RunWith(Parameterized.class)
public class StudentContactLogEntryPermissionTestsIT extends AbstractRESTPermissionsTest {
  
  public StudentContactLogEntryPermissionTestsIT(String role) {
    this.role = role;
  }
  
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  private StudentPermissions studentPermissions = new StudentPermissions();
  private StudentContactLogEntryPermissions studentContactLogEntryPermissions = new StudentContactLogEntryPermissions();
  
  private final static long TEST_STUDENT_ID = 3l;

  @Test
  public void testCreateStudentContactLogEntry() throws NoSuchFieldException {
    StudentContactLogEntry studentContactLogEntry = new StudentContactLogEntry(null, "create text", "creator name", getDate(2010, 3, 5), StudentContactLogEntryType.CHATLOG, Boolean.FALSE);
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(studentContactLogEntry)
      .post("/students/students/{ID}/contactLogEntries", TEST_STUDENT_ID);
    
    assertOk(response, studentContactLogEntryPermissions, StudentContactLogEntryPermissions.CREATE_STUDENTCONTACTLOGENTRY);

    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}?permanent=true", TEST_STUDENT_ID, id);
    }
  }

  @Test
  public void testListStudentContactLogEntries() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{ID}/contactLogEntries", TEST_STUDENT_ID);

    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentContactLogEntryPermissions, StudentContactLogEntryPermissions.LIST_STUDENTCONTACTLOGENTRIES, 403);
    }
    else {
      assertOk(response, studentContactLogEntryPermissions, StudentContactLogEntryPermissions.LIST_STUDENTCONTACTLOGENTRIES);
    }
  }
  
  @Test
  public void testFindStudentContactLogEntry() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/students/{STUDENTID}/contactLogEntries/{ID}", TEST_STUDENT_ID, 1l);
    if (roleIsAllowed(getRole(), studentPermissions, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      assertOk(response, studentContactLogEntryPermissions, StudentContactLogEntryPermissions.FIND_STUDENTCONTACTLOGENTRY, 403);
    }
    else {
      assertOk(response, studentContactLogEntryPermissions, StudentContactLogEntryPermissions.FIND_STUDENTCONTACTLOGENTRY);
    }
  }
  
  @Test
  public void testUpdateStudentContactLogEntry() throws NoSuchFieldException {
    StudentContactLogEntry studentContactLogEntry = new StudentContactLogEntry(null, "not updated text", "not updated creater", getDate(2010, 3, 5), StudentContactLogEntryType.CHATLOG, Boolean.FALSE);
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentContactLogEntry)
      .post("/students/students/{ID}/contactLogEntries", TEST_STUDENT_ID);
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      StudentContactLogEntry updateContactLogEntry = new StudentContactLogEntry(id, "updated text", "updated creater", getDate(2013, 3, 5), StudentContactLogEntryType.FACE2FACE, Boolean.FALSE);
      
      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateContactLogEntry)
        .put("/students/students/{STUDENTID}/contactLogEntries/{ID}", TEST_STUDENT_ID, id);

      assertOk(response, studentContactLogEntryPermissions, StudentContactLogEntryPermissions.UPDATE_STUDENTCONTACTLOGENTRY);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}?permanent=true", TEST_STUDENT_ID, id);
    }
  }
  
  @Test
  public void testDeleteStudentContactLogEntry() throws NoSuchFieldException {
    StudentContactLogEntry studentContactLogEntry = new StudentContactLogEntry(null, "create text", "creator name", getDate(2010, 3, 5), StudentContactLogEntryType.CHATLOG, Boolean.FALSE);
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(studentContactLogEntry)
      .post("/students/students/{ID}/contactLogEntries", TEST_STUDENT_ID);
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    response = given().headers(getAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}", TEST_STUDENT_ID, id);
    
    assertOk(response, studentContactLogEntryPermissions, StudentContactLogEntryPermissions.DELETE_STUDENTCONTACTLOGENTRY, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/students/{STUDENTID}/contactLogEntries/{ID}?permanent=true", TEST_STUDENT_ID, id);
  }
}
