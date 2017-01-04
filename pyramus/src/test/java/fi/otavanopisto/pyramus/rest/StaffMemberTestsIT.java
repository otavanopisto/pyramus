package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class StaffMemberTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testListStaffMembers() {
    given().headers(getAuthHeaders())
      .get("/staff/members")
      .then()
      .statusCode(200)
      .body("id.size()", is(10))
      .body("id[0]", is(1))
      .body("firstName[0]", is("Test Guest"))
      .body("lastName[0]", is("User #1"))
      .body("role[0]", is("GUEST"))
      .body("id[1]", is(2))
      .body("firstName[1]", is("Test Guest"))
      .body("lastName[1]", is("User #2"))
      .body("role[1]", is("GUEST"));
  }
  
  @Test
  public void testListStaffMembersByEmail() {
    given().headers(getAuthHeaders())
      .get("/staff/members?email=guest1@bogusmail.com")
      .then()
      .statusCode(200)
      .body("id.size()", is(1))
      .body("id[0]", is(1))
      .body("firstName[0]", is("Test Guest"))
      .body("lastName[0]", is("User #1"))
      .body("role[0]", is("GUEST"));
  }
  
  @Test
  public void testFindStaffMember() {
    given().headers(getAuthHeaders())
      .get("/staff/members/{ID}", 1l)
      .then()
      .statusCode(200)
      .body("id", is(1))
      .body("firstName", is("Test Guest"))
      .body("lastName", is("User #1"))
      .body("role", is("GUEST"));
  }
  
}
