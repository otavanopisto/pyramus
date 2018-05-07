package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupUserDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.PermissionFeatureHandler;

@PermissionFeature(PyramusPermissionFeatures.ONLY_OWN_GROUPS)
@RequestScoped
public class OnlyOwnGroupsPermissionFeature implements PermissionFeatureHandler {

  @Inject
  private Logger logger;
  
  @Inject
  private StudentGroupUserDAO studentGroupUserDAO;

  @Inject
  private StudentGroupStudentDAO studentGroupStudentDAO;
  
  @Inject
  private SessionController sessionController;
  
  @Override
  public boolean hasPermission(String perm, fi.otavanopisto.security.User user, ContextReference contextReference, boolean allowed) {
    // By default the permission needs to be allowed. This feature only disallows permission.
    if (!allowed || !sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION))
      return allowed;
   
    if (contextReference instanceof StudentGroup) {
      StudentGroup group = (StudentGroup) contextReference;
      
      if (user instanceof Student) {
        return studentGroupStudentDAO.findByStudentGroupAndStudent(group, (Student) user) != null;
      } else if (user instanceof StaffMember) {
        return studentGroupUserDAO.findByStudentGroupAndStaffMember(group, (StaffMember) user) != null;
      } else
        logger.log(Level.WARNING, "user not Student nor StaffMember, ignoring restrictions");
    } else
      logger.log(Level.WARNING, "ContextReference was not studentGroup, ignoring and returning default permission.");
    
    return allowed;
  }
  
}
