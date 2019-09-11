package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class MatriculationPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ STUDENT })
  public static final String LIST_EXAMS = "LIST_EXAMS";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ STUDENT, ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String GET_CURRENT_EXAM = "GET_CURRENT_EXAM";
  
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(MatriculationPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(MatriculationPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(MatriculationPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(MatriculationPermissions.class, permission);
  }
}
