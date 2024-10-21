package fi.otavanopisto.pyramus.security.impl;

import java.util.Date;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class StudentOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
  @Override
  public boolean handlesPermission(Permission permission) {
    if (permission != null)
      return (PermissionScope.STUDENT_OWNER.equals(permission.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    fi.otavanopisto.pyramus.domainmodel.users.User user1 = getUser(user);
    fi.otavanopisto.pyramus.domainmodel.users.User user2 = resolveUser(contextReference);
    
    if (user2 == null) {
      logger.warning(String.format("STUDENT_OWNER-scoped permission %s does not have User as context reference.", permission.getName()));
    }

    if (user1 != null && user2 != null) {
      // Users must match
      if (user1.getId().equals(user2.getId())) {
        if (Student.class.isInstance(user2)) {
          Student student = (Student) user2;
          
          return (student.getStudyEndDate() == null) || (student.getStudyEndDate().after(new Date()));
        }
      }
      return false;
    }
    
    return false;
  }

  @Override
  public boolean hasEveryonePermission(Permission permission, ContextReference contextReference) {
    return false;
  }

}
