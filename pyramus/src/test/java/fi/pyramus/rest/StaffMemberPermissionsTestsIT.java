package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fi.pyramus.rest.controller.permissions.UserPermissions;

@RunWith(Parameterized.class)
public class StaffMemberPermissionsTestsIT extends AbstractRESTPermissionsTest {

  private UserPermissions userPermissions = new UserPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public StaffMemberPermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testListStaffMembers() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/staff/members"), userPermissions, UserPermissions.LIST_STAFFMEMBERS, 200);
  }
  
  @Test
  public void testListStaffMembersByEmail() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/staff/members?email=guest1@bogusmail.com"), userPermissions, UserPermissions.LIST_STAFFMEMBER_EMAILS, 200);
  }
  
  @Test
  public void testFindStaffMember() throws NoSuchFieldException {
    assertOk(given().headers(getAuthHeaders())
      .get("/staff/members/{ID}", 1l), userPermissions, UserPermissions.FIND_STAFFMEMBER, 200);
  }
}
