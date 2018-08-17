package fi.otavanopisto.pyramus.security.impl;

import java.util.logging.Logger;

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
public class Permissions {

  @Inject
  private Logger logger;
  
  @Inject
  private PermissionDAO permissionDAO;
  
  @Inject
  @Any
  private Instance<PermissionResolver> permissionResolvers;
  
  public static Permissions instance() {
    return CDI.current().select(Permissions.class).get();
  }
  
  public boolean hasPermission(User user, String permissionName, ContextReference contextReference) {
    Permission permission = permissionDAO.findByName(permissionName);
    PermissionResolver permissionResolver = getPermissionResolver(permission);
    
    if (permissionResolver == null) {
      logger.severe(String.format("Could not find permissionResolver for permission %s", permission));
      return false;
    }

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
