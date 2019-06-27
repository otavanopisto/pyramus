package fi.otavanopisto.pyramus.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;

public class PyramusJUnitEnvironmentTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  /**
   * Ensures roleIsAllowed works as intented.
   */
  @Test
  public void testRoleIsAllowed() throws NoSuchFieldException {
    final StudentPermissions studentPermissions = new StudentPermissions();
    final String permission = StudentPermissions.CREATE_STUDENT;
    
    Role[] allRoles = Role.values();
    List<String> allRolesStr = Arrays.stream(allRoles).map(r -> r.name()).collect(Collectors.toList());

    // Use default roles from a permission that has both positive and negative outcomes
    List<String> defaultRoles = Arrays.asList(studentPermissions.getDefaultRoles(permission));
    
    // EVERYONE skews the permission checks
    assertFalse(defaultRoles.contains(Role.EVERYONE.name()), "Tested permission cannot contain EVERYONE");
    // Make sure there is a default role that we can test for positive outcome
    assertTrue(CollectionUtils.containsAny(allRolesStr, defaultRoles), "There are no roles to test positive outcome.");
    // Make sure there is a role that we can test for negative outcome
    assertFalse(CollectionUtils.isEqualCollection(allRolesStr, defaultRoles), "There are no roles to test negative outcome.");
    
    for (Role role : Role.values()) {
      String roleName = role.name();
      assertEquals(defaultRoles.contains(roleName), roleIsAllowed(role, studentPermissions, permission),
          String.format("roleIsAllowed doesn't match default roles list (%s)", roleName));
    }
  }
  
}
