package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.model.muikku.StaffMemberPayload;
import io.restassured.response.Response;


/**
 * Tests of permissions used in StudentRESTService
 */
@TestInstance(Lifecycle.PER_CLASS)
public class MuikkuRestServicePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  @DisplayName("Tests that allowed roles may create staffmembers.")
  @ParameterizedTest(name = "{index} => Role={0} creates Role= {1}")
  @MethodSource("createStaffMemberPermissionsSource")
  public void testCreateStaffMember(Role creatorRole, Role subjectRole, int expectedStatusCode) throws NoSuchFieldException {
    Response response = createStaffMember(creatorRole, subjectRole);
    
    assertThat(
        String.format("Status code <%d> didn't match expected code <%d> when Role = %s, createdStaffMemberRole = %s",
            response.statusCode(), expectedStatusCode, creatorRole, subjectRole),
        response.statusCode(), is(expectedStatusCode));
    
    if (response.getStatusCode() == 200) {
      long staffMemberId = response.body().jsonPath().getLong("identifier");
      long personId = getStaffMemberPersonId(staffMemberId);
      
      given().headers(getAdminAuthHeaders())
        .delete("/staff/members/{ID}?permanent=true", staffMemberId)
        .then().statusCode(204);
      given().headers(getAdminAuthHeaders())
        .delete("/persons/persons/{ID}", personId)
        .then().statusCode(204);
    }
  }

  @DisplayName("Tests that allowed roles may update staffmembers.")
  @ParameterizedTest(name = "{index} => Role={0} creates Role= {1}")
  @MethodSource("updateStaffMemberPermissionsSource")
  public void testUpdateStaffMember(Role modifierRole, Role subjectRole, Role updatedRole, int expectedStatusCode) throws NoSuchFieldException {
    Response response = createStaffMember(Role.ADMINISTRATOR, subjectRole);
    // No fancy assertions here, expect administrator being able to create the staffmember above
    response.then().statusCode(200);

    if (response.getStatusCode() == 200) {
      long staffMemberId = response.body().jsonPath().getLong("identifier");
      long personId = getStaffMemberPersonId(staffMemberId);
      
      response = testUpdateStaffMemberWithOptions(modifierRole, staffMemberId, updatedRole);
      
      assertThat(
          String.format("Status code <%d> didn't match expected code <%d> when Role = %s, createdStaffMemberRole = %s",
              response.statusCode(), expectedStatusCode, modifierRole, updatedRole),
          response.statusCode(), is(expectedStatusCode));
      
      given().headers(getAdminAuthHeaders())
        .delete("/staff/members/{ID}?permanent=true", staffMemberId)
        .then().statusCode(204);
      given().headers(getAdminAuthHeaders())
        .delete("/persons/persons/{ID}", personId)
        .then().statusCode(204);
    }
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> createStaffMemberPermissionsSource() {
    List<Arguments> testRoles = new ArrayList<>();

    // Roles that the endpoint allows for staffmembers
    EnumSet<Role> creatableRoles = EnumSet.of(Role.MANAGER, Role.TEACHER);
    EnumSet<Role> nonCreatableRoles = EnumSet.complementOf(creatableRoles);
    // Roles that are allowed to access the endpoint
    EnumSet<Role> allowedRoles = EnumSet.of(Role.ADMINISTRATOR, Role.MANAGER);
    EnumSet<Role> notAllowedRoles = EnumSet.complementOf(allowedRoles);

    // For each creatable role, test that allowed role can create them and non-allowed get 403
    creatableRoles.forEach(creatableRole -> {
      allowedRoles.forEach(allowedRole -> testRoles.add(Arguments.of(allowedRole, creatableRole, 200)));
      notAllowedRoles.forEach(notAllowedRole -> testRoles.add(Arguments.of(notAllowedRole, creatableRole, 403)));
    });
    
    // For each non-creatable role, test that allowed role gets 400 and non-allowed get 403
    nonCreatableRoles.forEach(nonCreatableRole -> {
      allowedRoles.forEach(allowedRole -> testRoles.add(Arguments.of(allowedRole, nonCreatableRole, 400)));
      notAllowedRoles.forEach(notAllowedRole -> testRoles.add(Arguments.of(notAllowedRole, nonCreatableRole, 403)));
    });
    
    return testRoles.stream();
  }
  
  @SuppressWarnings("unused")
  private static Stream<Arguments> updateStaffMemberPermissionsSource() {
    List<Arguments> testRoles = new ArrayList<>();

    // Roles that the endpoint allows for staffmembers
    EnumSet<Role> creatableRoles = EnumSet.of(Role.MANAGER, Role.TEACHER);
    EnumSet<Role> nonCreatableRoles = EnumSet.complementOf(creatableRoles);
    // Roles that are allowed to access the endpoint
    EnumSet<Role> allowedRoles = EnumSet.of(Role.ADMINISTRATOR, Role.MANAGER);
    EnumSet<Role> notAllowedRoles = EnumSet.complementOf(allowedRoles);

    // Test only starting from the creatable roles. Creation tests check for errors attempting to create with invalid role.
    creatableRoles.forEach(creatableRole -> {
      // Roles that can be created updated to same or another creatable role
      creatableRoles.forEach(creatableUpdatedRole -> {
        allowedRoles.forEach(allowedRole -> testRoles.add(Arguments.of(allowedRole, creatableRole, creatableUpdatedRole, 200)));
        notAllowedRoles.forEach(notAllowedRole -> testRoles.add(Arguments.of(notAllowedRole, creatableRole, creatableUpdatedRole, 403)));
      });
      
      // Roles that can be created updated to non creatable role
      nonCreatableRoles.forEach(creatableUpdatedRole -> {
        allowedRoles.forEach(allowedRole -> testRoles.add(Arguments.of(allowedRole, creatableRole, creatableUpdatedRole, 400)));
        notAllowedRoles.forEach(notAllowedRole -> testRoles.add(Arguments.of(notAllowedRole, creatableRole, creatableUpdatedRole, 403)));
      });
    });
    
    return testRoles.stream();
  }
  
  private Response createStaffMember(Role loggedInRole, Role createdStaffMemberRole) {
    StaffMemberPayload payload = new StaffMemberPayload();
    payload.setIdentifier(null);
    payload.setRoles(Arrays.asList(createdStaffMemberRole.toString()));
    payload.setFirstName("Muikku");
    payload.setLastName("Manager");
    payload.setEmail("muikkumanager@example.com");
    
    return given().headers(getAuthHeaders(loggedInRole))
        .contentType("application/json")
        .body(payload)
        .post("/muikku/users");
  }

  private Response testUpdateStaffMemberWithOptions(Role loggedInRole, long staffMemberId, Role updatedStaffMemberRole) {
    StaffMemberPayload payload = new StaffMemberPayload();
    payload.setIdentifier(String.valueOf(staffMemberId));
    payload.setRoles(Arrays.asList(updatedStaffMemberRole.toString()));
    payload.setFirstName("Maikki");
    payload.setLastName("Man-Ager");
    payload.setEmail("maikki@example.com");
    
    return given().headers(getAuthHeaders(loggedInRole))
        .contentType("application/json")
        .body(payload)
        .put("/muikku/users/{ID}", staffMemberId);
  }

  private long getStaffMemberPersonId(Long staffMemberId) {
    Response response = given().headers(getAdminAuthHeaders())
        .get("/staff/members/{ID}", staffMemberId);
      
    response.then().statusCode(200);
    
    return response.body().jsonPath().getLong("personId");
  }
  
}
