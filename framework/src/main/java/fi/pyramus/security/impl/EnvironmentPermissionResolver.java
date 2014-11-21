package fi.pyramus.security.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fi.muikku.security.ContextReference;
import fi.muikku.security.PermissionResolver;
import fi.muikku.security.User;
import fi.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.pyramus.dao.security.PermissionDAO;
import fi.pyramus.domainmodel.security.Permission;
import fi.pyramus.domainmodel.users.Role;

@Stateless
public class EnvironmentPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private PermissionDAO permissionDAO;
  
  @Inject
  private EnvironmentRolePermissionDAO environmentUserRolePermissionDAO;
  
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
    fi.pyramus.domainmodel.users.User userEntity = getUser(user);
    
    boolean allowed = environmentUserRolePermissionDAO.hasEnvironmentPermissionAccess(userEntity.getRole(), perm);
    System.out.println("Checking permission " + permission + " for: userId=" + userEntity.getId() + " role=" + userEntity.getRole() + " p=" + allowed);

    return allowed;
  }

  @Override
  public boolean hasEveryonePermission(String permission, ContextReference contextReference) {
    Role everyoneRole = getEveryoneRole();
    Permission perm = permissionDAO.findByName(permission);
    
    boolean allowed = environmentUserRolePermissionDAO.hasEnvironmentPermissionAccess(everyoneRole, perm);
    System.out.println("Checking permission " + permission + " for: everyone p=" + allowed);
    
    return allowed;
  }

}
