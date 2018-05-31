package fi.otavanopisto.pyramus.security.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class PermissionController {

  @Inject
  private PermissionDAO permissionDAO;
  
  @Inject
  @Any
  private Instance<PermissionResolver> permissionResolvers;
  
  public static PermissionController instance() {
    return CDI.current().select(PermissionController.class).get();
  }
  
  public boolean hasPermission(User user, String permissionName, ContextReference contextReference) {
    Permission permission = permissionDAO.findByName(permissionName);
    PermissionResolver permissionResolver = getPermissionResolver(permission);
    
    if (user != null) {
      return permissionResolver.hasPermission(permission, contextReference, user);
    } else {
      return permissionResolver.hasEveryonePermission(permission, contextReference);
    }
  }

  public boolean hasEnvironmentPermission(User user, String permission) {
    return hasPermission(user, permission, null);
  }

  private PermissionResolver getPermissionResolver(Permission permission) {
    for (PermissionResolver resolver : permissionResolvers) {
      if (resolver.handlesPermission(permission))
        return resolver;
    }
    
    return null;
  }
}
