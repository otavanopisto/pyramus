package fi.otavanopisto.pyramus.security.impl;

import javax.enterprise.context.ApplicationScoped;

import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class StudentParentPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

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
