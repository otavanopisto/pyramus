package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.CourseRoleArchetype;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class SystemPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, USER, GUEST, STUDENT, TEACHER, STUDY_GUIDER, STUDY_PROGRAMME_LEADER })
  public static final String WHOAMI = "WHOAMI";
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(SystemPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(SystemPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(SystemPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(SystemPermissions.class, permission);
  }

  @Override
  public CourseRoleArchetype[] getDefaultCourseRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultCourseRoles(SystemPermissions.class, permission);
  }
}
