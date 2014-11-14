package fi.pyramus.rest.controller.permissions;

import java.util.List;

import fi.muikku.security.Scope;
import fi.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.pyramus.security.impl.DefaultPermissionRoles;
import fi.pyramus.security.impl.PermissionScope;
import fi.pyramus.security.impl.PyramusPermissionCollection;

public class StudentActivityTypePermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_STUDENTACTIVITYTYPE = "CREATE_STUDENTACTIVITYTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, USER, GUEST, STUDENT })
  public static final String LIST_STUDENTACTIVITYTYPES = "LIST_STUDENTACTIVITYTYPES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, USER, GUEST, STUDENT })
  public static final String FIND_STUDENTACTIVITYTYPE = "FIND_STUDENTACTIVITYTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String UPDATE_STUDENTACTIVITYTYPE = "UPDATE_STUDENTACTIVITYTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTACTIVITYTYPE = "DELETE_STUDENTACTIVITYTYPE";
  
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(StudentActivityTypePermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(StudentActivityTypePermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(StudentActivityTypePermissions.class, permission);
  }

}
