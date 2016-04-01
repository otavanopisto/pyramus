package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.Scope;

public class StudentExaminationTypePermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String CREATE_STUDENTEXAMINATIONTYPE = "CREATE_STUDENTEXAMINATIONTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String LIST_STUDENTEXAMINATIONTYPES = "LIST_STUDENTEXAMINATIONTYPES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String FIND_STUDENTEXAMINATIONTYPE = "FIND_STUDENTEXAMINATIONTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER })
  public static final String UPDATE_STUDENTEXAMINATIONTYPE = "UPDATE_STUDENTEXAMINATIONTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTEXAMINATIONTYPE = "DELETE_STUDENTEXAMINATIONTYPE";
  
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(StudentExaminationTypePermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(StudentExaminationTypePermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(StudentExaminationTypePermissions.class, permission);
  }

}
