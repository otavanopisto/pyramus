package fi.otavanopisto.pyramus.security.impl;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class PersonOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
  @Override
  public boolean handlesPermission(Permission permission) {
    if (permission != null)
      return (PermissionScope.PERSON_OWNER.equals(permission.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    if (!(contextReference instanceof Person)) {
      logger.warning(String.format("PERSON_OWNER-scoped permission %s does not have Person as context reference.", permission.getName()));
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
