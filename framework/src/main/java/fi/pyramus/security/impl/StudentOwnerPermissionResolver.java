package fi.pyramus.security.impl;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fi.muikku.security.ContextReference;
import fi.muikku.security.PermissionResolver;
import fi.muikku.security.User;
import fi.pyramus.dao.security.PermissionDAO;
import fi.pyramus.domainmodel.security.Permission;
import fi.pyramus.domainmodel.students.Student;

@Stateless
public class StudentOwnerPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private PermissionDAO permissionDAO;
  
  @Override
  public boolean handlesPermission(String permission) {
    Permission perm = permissionDAO.findByName(permission);
    
    if (perm != null)
      return (PermissionScope.STUDENT_OWNER.equals(perm.getScope()));
    else
      return false;
  }

  @Override
  public boolean hasPermission(String permission, ContextReference contextReference, User user) {
    fi.pyramus.domainmodel.users.User user1 = getUser(user);
    fi.pyramus.domainmodel.users.User user2 = resolveUser(contextReference);
    
    System.out.println("Ownercheck: " + 
        user1 + (user1 != null ? "(" + user1.getId() + ")" : "") + 
        " vs " + 
        user2 + (user2 != null ? "(" + user2.getId() + ")" : "") +
        " @ " + 
        contextReference);
    
    if ((user1 != null) && (user2 != null)) {
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
  public boolean hasEveryonePermission(String permission, ContextReference contextReference) {
    return false;
  }

}
