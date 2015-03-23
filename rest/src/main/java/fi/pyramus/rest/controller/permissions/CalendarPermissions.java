package fi.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.security.Scope;
import fi.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.pyramus.security.impl.DefaultPermissionRoles;
import fi.pyramus.security.impl.PermissionScope;
import fi.pyramus.security.impl.PyramusPermissionCollection;

public class CalendarPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_ACADEMICTERM = "CREATE_ACADEMICTERM";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String LIST_ACADEMICTERMS = "LIST_ACADEMICTERMS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String FIND_ACADEMICTERM = "FIND_ACADEMICTERM";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String UPDATE_ACADEMICTERM = "UPDATE_ACADEMICTERM";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_ACADEMICTERM = "DELETE_ACADEMICTERM";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, USER, GUEST, STUDENT })
  public static final String FIND_COURSESBYACADEMICTERM = "FIND_COURSESBYACADEMICTERM";

  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(CalendarPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(CalendarPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(CalendarPermissions.class, permission);
  }

}
