package fi.pyramus.security.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fi.muikku.security.ContextReference;
import fi.muikku.security.PermissionResolver;
import fi.muikku.security.User;
import fi.pyramus.dao.security.PermissionDAO;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.security.Permission;

@Stateless
public class PersonOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private PermissionDAO permissionDAO;
  
  @Override
  public boolean handlesPermission(String permission) {
    Permission perm = permissionDAO.findByName(permission);
    
    if (perm != null)
      return (PermissionScope.PERSON_OWNER.equals(perm.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(String permission, ContextReference contextReference, User user) {
    if (!(contextReference instanceof Person)) {
      return false;
    }
    
    Person person = (Person) contextReference;
    
    fi.pyramus.domainmodel.users.User pyramusUser = getUser(user);
    if (pyramusUser.getPerson().getId().equals(person.getId())) {
      return true;
    }
    
    return false;
  }

  @Override
  public boolean hasEveryonePermission(String permission, ContextReference contextReference) {
    return false;
  }

}
