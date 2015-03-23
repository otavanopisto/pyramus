package fi.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.security.Scope;
import fi.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.pyramus.security.impl.DefaultPermissionRoles;
import fi.pyramus.security.impl.PermissionScope;
import fi.pyramus.security.impl.PyramusPermissionCollection;

public class ProjectPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_PROJECT = "CREATE_PROJECT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String LIST_PROJECTS = "LIST_PROJECTS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String FIND_PROJECT = "FIND_PROJECT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String UPDATE_PROJECT = "UPDATE_PROJECT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_PROJECT = "DELETE_PROJECT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_PROJECTMODULE = "CREATE_PROJECTMODULE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String LIST_PROJECTMODULES = "LIST_PROJECTMODULES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String FIND_PROJECTMODULE = "FIND_PROJECTMODULE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String UPDATE_PROJECTMODULE = "UPDATE_PROJECTMODULE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_PROJECTMODULE = "DELETE_PROJECTMODULE";
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(ProjectPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(ProjectPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(ProjectPermissions.class, permission);
  }

}
