package fi.otavanopisto.pyramus.security.impl;

import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class UserOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Override
  public boolean handlesPermission(Permission permission) {
    if (permission != null)
      return (PermissionScope.USER_OWNER.equals(permission.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    fi.otavanopisto.pyramus.domainmodel.users.User user1 = getUser(user);
    fi.otavanopisto.pyramus.domainmodel.users.User user2 = resolveUser(contextReference);
    if (user1 != null && user2 != null) {
      // The underlying persons of the users must match
      return user1.getPerson().getId().equals(user2.getPerson().getId());
    }
    return false;
  }

  @Override
  public boolean hasEveryonePermission(Permission permission, ContextReference contextReference) {
    return false;
  }

}
