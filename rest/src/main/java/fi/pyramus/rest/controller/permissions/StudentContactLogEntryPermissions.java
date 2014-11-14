package fi.pyramus.rest.controller.permissions;

import java.util.List;

import fi.muikku.security.Scope;
import fi.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.pyramus.security.impl.DefaultPermissionRoles;
import fi.pyramus.security.impl.PermissionScope;
import fi.pyramus.security.impl.PyramusPermissionCollection;

public class StudentContactLogEntryPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_STUDENTCONTACTLOGENTRY = "CREATE_STUDENTCONTACTLOGENTRY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, USER, GUEST, STUDENT })
  public static final String LIST_STUDENTCONTACTLOGENTRIES = "LIST_STUDENTCONTACTLOGENTRIES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, USER, GUEST, STUDENT })
  public static final String FIND_STUDENTCONTACTLOGENTRY = "FIND_STUDENTCONTACTLOGENTRY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String UPDATE_STUDENTCONTACTLOGENTRY = "UPDATE_STUDENTCONTACTLOGENTRY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTCONTACTLOGENTRY = "DELETE_STUDENTCONTACTLOGENTRY";
  
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(StudentContactLogEntryPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(StudentContactLogEntryPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(StudentContactLogEntryPermissions.class, permission);
  }

}
