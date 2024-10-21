package fi.otavanopisto.pyramus.security.impl;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class StudentParentPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
  @Override
  public boolean handlesPermission(Permission permission) {
    if (permission != null)
      return (PermissionScope.STUDENT_PARENT.equals(permission.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    if (user instanceof StudentParent) {
      StudentParent studentParent = (StudentParent) user;
      
      fi.otavanopisto.pyramus.domainmodel.users.User contextReferenceUser = resolveUser(contextReference);

      if (contextReferenceUser == null) {
        logger.warning(String.format("STUDENT_PARENT-scoped permission %s does not have User as context reference.", permission.getName()));
      }

      if (contextReferenceUser instanceof Student) {
        Student student = (Student) contextReferenceUser;
        return studentParent.isActiveParentOf(student);
      }
    }

    return false;
  }

  @Override
  public boolean hasEveryonePermission(Permission permission, ContextReference contextReference) {
    return false;
  }

}
