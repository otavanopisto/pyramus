package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.CourseRoleArchetype;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class MunicipalityPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_MUNICIPALITY = "CREATE_MUNICIPALITY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String LIST_MUNICIPALITIES = "LIST_MUNICIPALITIES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String FIND_MUNICIPALITY = "FIND_MUNICIPALITY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_MUNICIPALITY = "UPDATE_MUNICIPALITY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_MUNICIPALITY = "DELETE_MUNICIPALITY";
  
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(MunicipalityPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(MunicipalityPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(MunicipalityPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(MunicipalityPermissions.class, permission);
  }

  @Override
  public CourseRoleArchetype[] getDefaultCourseRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultCourseRoles(MunicipalityPermissions.class, permission);
  }
}
