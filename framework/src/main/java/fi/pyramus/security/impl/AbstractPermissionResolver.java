package fi.pyramus.security.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fi.pyramus.dao.users.SystemRoleEntityDAO;
import fi.pyramus.domainmodel.users.RoleEntity;
import fi.pyramus.domainmodel.users.SystemRoleType;
import fi.pyramus.domainmodel.users.User;

@Stateless
public abstract class AbstractPermissionResolver {

  @Inject
  private SystemRoleEntityDAO systemUserRoleDAO;

  protected RoleEntity getEveryoneRole() {
    return systemUserRoleDAO.findByRoleType(SystemRoleType.EVERYONE);
  }
  
  protected User getUser(fi.muikku.security.User user) {
    return (User) user;
  }

}
