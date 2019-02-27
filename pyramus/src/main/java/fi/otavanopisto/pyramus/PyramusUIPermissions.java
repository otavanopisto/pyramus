package fi.otavanopisto.pyramus;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.CourseRoleArchetype;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class PyramusUIPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String VIEW_STUDENT = "VIEW_STUDENT";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String EDIT_STUDENT = "EDIT_STUDENT";

  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(PyramusUIPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(PyramusUIPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(PyramusUIPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(PyramusUIPermissions.class, permission);
  }

  @Override
  public CourseRoleArchetype[] getDefaultCourseRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultCourseRoles(PyramusUIPermissions.class, permission);
  }
}
