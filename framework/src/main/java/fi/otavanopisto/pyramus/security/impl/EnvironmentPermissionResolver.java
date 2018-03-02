package fi.otavanopisto.pyramus.security.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.PermissionFeatureHandler;
import fi.otavanopisto.security.PermissionFeatureLiteral;
import fi.otavanopisto.security.User;

@Stateless
public class EnvironmentPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
  @Inject
  private EnvironmentRolePermissionDAO environmentUserRolePermissionDAO;

  @Inject
  @Any
  private Instance<PermissionFeatureHandler> featureHandlers;
  
  @Override
  public boolean handlesPermission(Permission permission) {
    if (permission != null)
      return (PermissionScope.ENVIRONMENT.equals(permission.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    fi.otavanopisto.pyramus.domainmodel.users.User userEntity = getUser(user);
    boolean allowed = environmentUserRolePermissionDAO.hasEnvironmentPermissionAccess(userEntity.getRole(), permission);
    if (!allowed) {
      allowed = hasEveryonePermission(permission, contextReference);
    }
    
    PyramusPermissionCollection collection = findCollection(permission.getName());
    try {
      PermissionFeature[] features = collection.listPermissionFeatures(permission.getName());
      if (features != null) {
        for (PermissionFeature feature : features) {
          Instance<PermissionFeatureHandler> instance = featureHandlers.select(new PermissionFeatureLiteral(feature.value()));
          if (!instance.isUnsatisfied()) {
            PermissionFeatureHandler permissionFeatureHandler = instance.get();
            allowed = permissionFeatureHandler.hasPermission(permission.getName(), userEntity, contextReference, allowed);
          } else
            logger.log(Level.SEVERE, String.format("Unsatisfied permission feature %s", feature.value()));
        }
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, String.format("Could not list permission features for permission %s", permission), e);
    }
    
    return allowed;
  }

  @Override
  public boolean hasEveryonePermission(Permission permission, ContextReference contextReference) {
    Role everyoneRole = getEveryoneRole();
    return environmentUserRolePermissionDAO.hasEnvironmentPermissionAccess(everyoneRole, permission);
  }

}
