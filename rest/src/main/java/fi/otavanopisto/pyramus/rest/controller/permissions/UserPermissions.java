package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class UserPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.USER_OWNER)
  public static final String USER_OWNER = "USER_OWNER";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String CREATE_STAFFMEMBER = "CREATE_STAFFMEMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_STAFFMEMBERS = "LIST_STAFFMEMBERS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_STAFFMEMBER = "FIND_STAFFMEMBER";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_STAFFMEMBER_EMAILS = "LIST_STAFFMEMBER_EMAILS";

  /**
   * UserVariable
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_USERVARIABLE = "CREATE_USERVARIABLE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_USERVARIABLE = "FIND_USERVARIABLE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_USERVARIABLES = "LIST_USERVARIABLES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_USERVARIABLE = "UPDATE_USERVARIABLE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_USERVARIABLES = "UPDATE_USERVARIABLES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_USERVARIABLE = "DELETE_USERVARIABLE";
  
  /**
   * UserVariableKey
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_USERVARIABLEKEY = "CREATE_USERVARIABLEKEY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_USERVARIABLEKEY = "FIND_USERVARIABLEKEY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_USERVARIABLEKEYS = "LIST_USERVARIABLEKEYS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_USERVARIABLEKEY = "UPDATE_USERVARIABLEKEY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_USERVARIABLEKEY = "DELETE_USERVARIABLEKEY";
  
  /**
   * staff member addresses
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STAFFMEMBERADDRESS = "CREATE_STAFFMEMBERADDRESS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STAFFMEMBERADDRESSES = "LIST_STAFFMEMBERADDRESSES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_STAFFMEMBERADDRESS = "FIND_STAFFMEMBERADDRESS";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_STAFFMEMBERADDRESS = "UPDATE_STAFFMEMBERADDRESS";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STAFFMEMBERADDRESS = "DELETE_STAFFMEMBERADDRESS";
  
  /*
   * Phone numbers
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STAFFMEMBERPHONENUMBER = "CREATE_STAFFMEMBERPHONENUMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STAFFMEMBERPHONENUMBERS = "LIST_STAFFMEMBERPHONENUMBERS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_STAFFMEMBERPHONENUMBER = "FIND_STAFFMEMBERPHONENUMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_STAFFMEMBERPHONENUMBER = "UPDATE_STAFFMEMBERPHONENUMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STAFFMEMBERPHONENUMBER = "DELETE_STAFFMEMBERPHONENUMBER";
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(UserPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(UserPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(UserPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(UserPermissions.class, permission);
  }
}
