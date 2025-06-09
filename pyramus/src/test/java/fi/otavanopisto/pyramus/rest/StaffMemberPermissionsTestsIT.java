package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;

public class StaffMemberPermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private UserPermissions userPermissions = new UserPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStaffMembers(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/staff/members"), userPermissions, UserPermissions.LIST_STAFFMEMBERS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStaffMembersByEmail(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/staff/members?email=guest1@bogusmail.com"), userPermissions, UserPermissions.LIST_STAFFMEMBERS, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindStaffMember(Role role) throws NoSuchFieldException {
    assertOk(role, given().headers(getAuthHeaders(role))
      .get("/staff/members/{ID}", 1l), userPermissions, UserPermissions.FIND_STAFFMEMBER, 200);
  }
}
