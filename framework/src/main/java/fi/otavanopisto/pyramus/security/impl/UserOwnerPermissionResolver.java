package fi.otavanopisto.pyramus.security.impl;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class UserOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
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
    if (user2 == null) {
      logger.warning(String.format("USER_OWNER-scoped permission %s does not have User as context reference.", permission.getName()));
    }
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
