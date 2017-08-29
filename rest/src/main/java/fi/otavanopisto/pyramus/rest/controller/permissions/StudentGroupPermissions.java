package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class StudentGroupPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTGROUP = "CREATE_STUDENTGROUP";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_STUDENTGROUPS = "LIST_STUDENTGROUPS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_STUDENTGROUPSTUDENTS = "LIST_STUDENTGROUPSTUDENTS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_STUDENTGROUPSTAFFMEMBERS = "LIST_STUDENTGROUPSTAFFMEMBERS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  @PermissionFeature(PyramusPermissionFeatures.ONLY_OWN_GROUPS)
  public static final String FIND_STUDENTGROUP = "FIND_STUDENTGROUP";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_STUDENTGROUP = "UPDATE_STUDENTGROUP";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTGROUP = "DELETE_STUDENTGROUP";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTGROUPSTUDENT = "CREATE_STUDENTGROUPSTUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_STUDENTGROUPSTUDENT = "FIND_STUDENTGROUPSTUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTGROUPSTUDENT = "DELETE_STUDENTGROUPSTUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTGROUPSTAFFMEMBER = "CREATE_STUDENTGROUPSTAFFMEMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_STUDENTGROUPSTAFFMEMBER = "FIND_STUDENTGROUPSTAFFMEMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTGROUPSTAFFMEMBER = "DELETE_STUDENTGROUPSTAFFMEMBER";
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(StudentGroupPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(StudentGroupPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(StudentGroupPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(StudentGroupPermissions.class, permission);
  }
}
