package fi.otavanopisto.pyramus.views;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.CourseRoleArchetype;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class PyramusViewPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String SEARCH_STUDENTS = "SEARCH_STUDENTS";
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(PyramusViewPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(PyramusViewPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(PyramusViewPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(PyramusViewPermissions.class, permission);
  }

  @Override
  public CourseRoleArchetype[] getDefaultCourseRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultCourseRoles(PyramusViewPermissions.class, permission);
  }
}
