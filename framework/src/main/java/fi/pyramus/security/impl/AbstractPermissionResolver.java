package fi.pyramus.security.impl;

import javax.ejb.Stateless;

import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;

@Stateless
public abstract class AbstractPermissionResolver {

  protected Role getEveryoneRole() {
    return Role.EVERYONE;
  }
  
  protected User getUser(fi.muikku.security.User user) {
    return (User) user;
  }

}
