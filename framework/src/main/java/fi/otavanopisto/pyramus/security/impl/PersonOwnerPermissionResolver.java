package fi.otavanopisto.pyramus.security.impl;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@Stateless
public class PersonOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Override
  public boolean handlesPermission(Permission perm) {
    if (perm != null)
      return (PermissionScope.PERSON_OWNER.equals(perm.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    if (!(contextReference instanceof Person)) {
      return false;
    }
    
    Person person = (Person) contextReference;
    
    fi.otavanopisto.pyramus.domainmodel.users.User pyramusUser = getUser(user);
    if (pyramusUser.getPerson().getId().equals(person.getId())) {
      return true;
    }
    
    return false;
  }

  @Override
  public boolean hasEveryonePermission(Permission permission, ContextReference contextReference) {
    return false;
  }

}
