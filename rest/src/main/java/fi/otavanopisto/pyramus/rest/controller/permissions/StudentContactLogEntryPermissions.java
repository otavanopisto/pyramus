package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class StudentContactLogEntryPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTCONTACTLOGENTRY = "CREATE_STUDENTCONTACTLOGENTRY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STUDENTCONTACTLOGENTRIES = "LIST_STUDENTCONTACTLOGENTRIES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_STUDENTCONTACTLOGENTRY = "FIND_STUDENTCONTACTLOGENTRY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
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

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(StudentContactLogEntryPermissions.class, permission);
  }
}
