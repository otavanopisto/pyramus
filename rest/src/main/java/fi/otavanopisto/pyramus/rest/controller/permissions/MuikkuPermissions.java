package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.CourseRoleArchetype;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionFeatures;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class MuikkuPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_CREATE_STAFF_MEMBER = "MUIKKU_CREATE_STAFF_MEMBER";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_UPDATE_STAFF_MEMBER = "MUIKKU_UPDATE_STAFF_MEMBER";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_CREATE_STUDENT = "MUIKKU_CREATE_STUDENT";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_UPDATE_STUDENT = "MUIKKU_UPDATE_STUDENT";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_CREATE_STUDENT_GROUP = "MUIKKU_CREATE_STUDENT_GROUP";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_UPDATE_STUDENT_GROUP = "MUIKKU_UPDATE_STUDENT_GROUP";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_DELETE_STUDENT_GROUP = "MUIKKU_DELETE_STUDENT_GROUP";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_ADD_STUDENT_GROUP_MEMBERS = "MUIKKU_ADD_STUDENT_GROUP_MEMBERS";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String MUIKKU_REMOVE_STUDENT_GROUP_MEMBERS = "MUIKKU_REMOVE_STUDENT_GROUP_MEMBERS";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ TRUSTED_SYSTEM })
  public static final String MUIKKU_REQUEST_CREDENTIAL_RESET = "MUIKKU_REQUEST_CREDENTIAL_RESET";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ TRUSTED_SYSTEM })
  public static final String MUIKKU_RESET_CREDENTIALS = "MUIKKU_RESET_CREDENTIALS";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  @PermissionFeature(PyramusPermissionFeatures.ONLY_GROUP_STUDENTS)
  public static final String GET_STUDENT_COURSE_ACTIVITY = "GET_STUDENT_COURSE_ACTIVITY";
   
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(MuikkuPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(MuikkuPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(MuikkuPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(MuikkuPermissions.class, permission);
  }

  @Override
  public CourseRoleArchetype[] getDefaultCourseRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultCourseRoles(MuikkuPermissions.class, permission);
  }

}