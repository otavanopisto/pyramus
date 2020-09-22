package fi.otavanopisto.pyramus.security.impl.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.CourseRoleArchetype;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class CourseSignupGroupPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_SIGNUP_STUDYPROGRAMME = "CREATE_SIGNUP_STUDYPROGRAMME";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM })
  public static final String LIST_SIGNUP_STUDYPROGRAMMES = "LIST_SIGNUP_STUDYPROGRAMMES";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM })
  public static final String FIND_SIGNUP_STUDYPROGRAMME = "FIND_SIGNUP_STUDYPROGRAMME";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String DELETE_SIGNUP_STUDYPROGRAMME = "DELETE_SIGNUP_STUDYPROGRAMME";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_SIGNUP_STUDENTGROUP = "CREATE_SIGNUP_STUDENTGROUP";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM })
  public static final String LIST_SIGNUP_STUDENTGROUPS = "LIST_SIGNUP_STUDENTGROUPS";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM })
  public static final String FIND_SIGNUP_STUDENTGROUP = "FIND_SIGNUP_STUDENTGROUP";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String DELETE_SIGNUP_STUDENTGROUP = "DELETE_SIGNUP_STUDENTGROUP";

  @Override
  public List<String> listPermissions() {
    return super.listPermissions(CourseSignupGroupPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(CourseSignupGroupPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(CourseSignupGroupPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(CourseSignupGroupPermissions.class, permission);
  }

  @Override
  public CourseRoleArchetype[] getDefaultCourseRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultCourseRoles(CourseSignupGroupPermissions.class, permission);
  }
}
