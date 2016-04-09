package fi.otavanopisto.pyramus.security.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.PermissionResolver;
import fi.otavanopisto.security.User;

@Stateless
public class UserOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private PermissionDAO permissionDAO;
  
  @Override
  public boolean handlesPermission(String permission) {
    Permission perm = permissionDAO.findByName(permission);
    
    if (perm != null)
      return (PermissionScope.USER_OWNER.equals(perm.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(String permission, ContextReference contextReference, User user) {
    fi.otavanopisto.pyramus.domainmodel.users.User user1 = getUser(user);
    fi.otavanopisto.pyramus.domainmodel.users.User user2 = resolveUser(contextReference);
    if (user1 != null && user2 != null) {
      // The underlying persons of the users must match
      return user1.getPerson().getId().equals(user2.getPerson().getId());
    }
    return false;
  }

  @Override
  public boolean hasEveryonePermission(String permission, ContextReference contextReference) {
    return false;
  }

}
