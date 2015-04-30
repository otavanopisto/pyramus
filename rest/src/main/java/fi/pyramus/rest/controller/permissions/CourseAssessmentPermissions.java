package fi.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.security.Scope;
import fi.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.pyramus.security.impl.DefaultPermissionRoles;
import fi.pyramus.security.impl.PermissionScope;
import fi.pyramus.security.impl.PyramusPermissionCollection;

public class CourseAssessmentPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {


  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_COURSEASSESSMENT = "CREATE_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER})
  public static final String UPDATE_COURSEASSESSMENT = "UPDATE_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String LIST_COURSEASSESSMENT = "LIST_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String FIND_COURSEASSESSMENT = "FIND_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER})
  public static final String DELETE_COURSEASSESSMENT = "DELETE_COURSEASSESSMENT";

  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(CourseAssessmentPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(CourseAssessmentPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(CourseAssessmentPermissions.class, permission);
  }

}
