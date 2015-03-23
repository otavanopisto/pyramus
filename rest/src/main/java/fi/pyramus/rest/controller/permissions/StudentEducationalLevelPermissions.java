package fi.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.security.Scope;
import fi.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.pyramus.security.impl.DefaultPermissionRoles;
import fi.pyramus.security.impl.PermissionScope;
import fi.pyramus.security.impl.PyramusPermissionCollection;

public class StudentEducationalLevelPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_STUDENTEDUCATIONALLEVEL = "CREATE_STUDENTEDUCATIONALLEVEL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String LIST_STUDENTEDUCATIONALLEVELS = "LIST_STUDENTEDUCATIONALLEVELS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String FIND_STUDENTEDUCATIONALLEVEL = "FIND_STUDENTEDUCATIONALLEVEL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String UPDATE_STUDENTEDUCATIONALLEVEL = "UPDATE_STUDENTEDUCATIONALLEVEL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTEDUCATIONALLEVEL = "DELETE_STUDENTEDUCATIONALLEVEL";
  
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(StudentEducationalLevelPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(StudentEducationalLevelPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(StudentEducationalLevelPermissions.class, permission);
  }

}
