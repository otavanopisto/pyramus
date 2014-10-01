package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class UserTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testListUsers() {
    given().headers(getAuthHeaders())
      .get("/users/users")
      .then()
      .statusCode(200)
      .body("id.size()", is(4))
      .body("id[0]", is(1))
      .body("firstName[0]", is("Test Guest"))
      .body("lastName[0]", is("User #1"))
      .body("role[0]", is("GUEST"))
      .body("id[1]", is(2))
      .body("firstName[1]", is("Test Guest"))
      .body("lastName[1]", is("User #2"))
      .body("role[1]", is("GUEST"))
      
      .body("id[2]", is(3))
      .body("firstName[2]", is("Tanya"))
      .body("lastName[2]", is("Test #1"))
      .body("role[2]", is("STUDENT"))
      .body("id[3]", is(4))
      .body("firstName[3]", is("David"))
      .body("lastName[3]", is("Test #2"))
      .body("role[3]", is("STUDENT"));
  }
  
  @Test
  public void testFindUser() {
    given().headers(getAuthHeaders())
      .get("/users/users/{ID}", 1l)
      .then()
      .statusCode(200)
      .body("id", is(1))
      .body("firstName", is("Test Guest"))
      .body("lastName", is("User #1"))
      .body("role", is("GUEST"));
  }
  
}
