package fi.otavanopisto.pyramus.security.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.PermissionResolver;
import fi.otavanopisto.security.User;

@Stateless
public class EnvironmentPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
  @Inject
  private PermissionDAO permissionDAO;
  
  @Inject
  private EnvironmentRolePermissionDAO environmentUserRolePermissionDAO;

  @Inject
  @Any
  private Instance<PermissionFeatureHandler> featureHandlers;
  
  @Override
  public boolean handlesPermission(String permission) {
    Permission perm = permissionDAO.findByName(permission);
    
    if (perm != null)
      return (PermissionScope.ENVIRONMENT.equals(perm.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(String permission, ContextReference contextReference, User user) {
    Permission perm = permissionDAO.findByName(permission);
    fi.otavanopisto.pyramus.domainmodel.users.User userEntity = getUser(user);
    boolean allowed = environmentUserRolePermissionDAO.hasEnvironmentPermissionAccess(userEntity.getRole(), perm);
    if (!allowed) {
      allowed = hasEveryonePermission(permission, contextReference);
    }
    
    PyramusPermissionCollection collection = findCollection(permission);
    try {
      PermissionFeatures features = collection.listPermissionFeatures(permission);
      if (features != null) {
        for (PermissionFeature feature : features.value()) {
          Instance<PermissionFeatureHandler> instance = featureHandlers.select(new PermissionFeatureLiteral(feature.value()));
          if (!instance.isUnsatisfied()) {
            PermissionFeatureHandler permissionFeatureHandler = instance.get();
            allowed = permissionFeatureHandler.hasPermission(perm, userEntity, contextReference, allowed);
          } else
            logger.log(Level.SEVERE, String.format("Unsatisfied permission feature %s", feature.value()));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return allowed;
  }

  @Override
  public boolean hasEveryonePermission(String permission, ContextReference contextReference) {
    Role everyoneRole = getEveryoneRole();
    Permission perm = permissionDAO.findByName(permission);
    return environmentUserRolePermissionDAO.hasEnvironmentPermissionAccess(everyoneRole, perm);
  }

}
