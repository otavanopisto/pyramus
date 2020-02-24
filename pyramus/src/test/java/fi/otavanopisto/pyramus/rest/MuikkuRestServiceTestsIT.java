package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import io.restassured.response.Response;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.model.muikku.StaffMemberPayload;

public class MuikkuRestServiceTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStaffMember() {
    Role testStaffMemberRole = Role.MANAGER;
    StaffMemberPayload payload = new StaffMemberPayload();
    payload.setIdentifier(null);
    payload.setRole(testStaffMemberRole.toString());
    payload.setFirstName("Muikku");
    payload.setLastName("Manager");
    payload.setEmail("muikkumanager@example.com");
    
    Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(payload)
        .post("/muikku/users");
    
    response.then()
      .statusCode(200)
      .body("identifier", not(is((Long) null)))
      .body("firstName", is(payload.getFirstName()))
      .body("lastName", is(payload.getLastName()))
      .body("email", is(payload.getEmail()))
      .body("role", is(testStaffMemberRole.toString()));
      
    long id = response.body().jsonPath().getLong("identifier");
    
    given().headers(getAuthHeaders())
      .delete("/staff/members/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testCreateStaffMemberWithReservedEmail() {
    Role testStaffMemberRole = Role.MANAGER;
    StaffMemberPayload payload = new StaffMemberPayload();
    payload.setIdentifier(null);
    payload.setRole(testStaffMemberRole.toString());
    payload.setFirstName("Muikku");
    payload.setLastName("Manager");
    payload.setEmail("muikkumanager@example.com");
    
    Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(payload)
        .post("/muikku/users");
    
    response.then()
      .statusCode(200)
      .body("identifier", not(is((Long) null)));
      
    long id = response.body().jsonPath().getLong("identifier");
    
    try {
      payload.setIdentifier(null);
      payload.setFirstName("Muikku2");
      payload.setLastName("Manager2");
      
      // Attempt to create user with same email - the result should be 409
      given().headers(getAuthHeaders())
          .contentType("application/json")
          .body(payload)
          .post("/muikku/users")
          .then()
          .statusCode(409);
    } finally {
      given().headers(getAuthHeaders())
        .delete("/staff/members/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testUpdateStaffMember() {
    Role testStaffMemberRole = Role.TEACHER;
    StaffMemberPayload payload = new StaffMemberPayload();
    payload.setIdentifier(null);
    payload.setRole(testStaffMemberRole.toString());
    payload.setFirstName("Muikku");
    payload.setLastName("Manager");
    payload.setEmail("muikkumanager@example.com");
    
    Response response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(payload)
        .post("/muikku/users");
    
    response.then()
      .statusCode(200)
      .body("identifier", not(is((Long) null)))
      .body("firstName", is(payload.getFirstName()))
      .body("lastName", is(payload.getLastName()))
      .body("email", is(payload.getEmail()))
      .body("role", is(testStaffMemberRole.toString()));
      
    long id = response.body().jsonPath().getLong("identifier");
    try {
      Role testUpdatedStaffMemberRole = Role.MANAGER;
      payload = new StaffMemberPayload();
      payload.setIdentifier(String.valueOf(id));
      payload.setRole(testUpdatedStaffMemberRole.toString());
      payload.setFirstName("Muikku");
      payload.setLastName("Manager");
      payload.setEmail("muikkumanager@example.com");
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(payload)
        .put("/muikku/users/{ID}", id)
        .then()
        .statusCode(200)
        .body("identifier", is(payload.getIdentifier()))
        .body("firstName", is(payload.getFirstName()))
        .body("lastName", is(payload.getLastName()))
        .body("email", is(payload.getEmail()))
        .body("role", is(testUpdatedStaffMemberRole.toString()));
    } finally {
      given().headers(getAuthHeaders())
        .delete("/staff/members/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
}
