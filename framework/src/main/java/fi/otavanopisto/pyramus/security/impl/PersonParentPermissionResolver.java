package fi.otavanopisto.pyramus.security.impl;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class PersonParentPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
  @Override
  public boolean handlesPermission(Permission permission) {
    return permission != null ? PermissionScope.PERSON_PARENT.equals(permission.getScope()) : false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    if (!(contextReference instanceof Person)) {
      logger.warning(String.format("PERSON_PARENT-scoped permission %s does not have Person as context reference.", permission.getName()));
      return false;
    }
    
    fi.otavanopisto.pyramus.domainmodel.users.User pyramusUser = getUser(user);
    if (pyramusUser instanceof StudentParent) {
      StudentParent studentParent = (StudentParent) pyramusUser;

      Person person = (Person) contextReference;

      for (Student student : person.getStudents()) {
        if (studentParent.isActiveParentOf(student)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public boolean hasEveryonePermission(Permission permission, ContextReference contextReference) {
    return false;
  }

}
