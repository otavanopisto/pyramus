package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionFeatures;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.PermissionFeatureHandler;

/**
 * Allows permission, if the given student (from contextReference) is
 * on a course where the user is a teacher on.
 */
@PermissionFeature(PyramusPermissionFeatures.ALLOW_COURSE_TEACHER)
@RequestScoped
public class AllowCourseTeacherPermissionFeature extends AbstractPermissionFeature implements PermissionFeatureHandler {

  @Inject
  private Logger logger;
  
  @Inject
  private StudentDAO studentDAO;

  @Override
  public boolean hasPermission(String permission, fi.otavanopisto.security.User user, ContextReference contextReference, boolean allowed) {
    // If the permission is already allowed, we don't need to check anything
    if (allowed) {
      return true;
    }
    
    User maybeStudent = resolveUser(contextReference);
    
    if (maybeStudent instanceof Student && user instanceof StaffMember) {
      boolean haveCommonCourses = studentDAO.haveCommonCourses((StaffMember) user, (Student) maybeStudent);
      return haveCommonCourses;
    } else {
      // If contextReference is not a student, log a warning as it means the 
      // permission is used somewhere without specifying proper contextReference
      if (!(maybeStudent instanceof Student)) {
        String contextStr = contextReference == null ? "null" : contextReference.getClass().getSimpleName();
        logger.log(Level.WARNING, String.format("ContextReference %s was not student, ignoring and returning default permission", contextStr));
      }
      
      // Couldn't resolve the course teacher <-> student status so default to the provided state
      return allowed;
    }
  }
  
}
