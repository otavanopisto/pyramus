package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.security.impl.PermissionController;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionFeatures;
import fi.otavanopisto.pyramus.security.impl.UserContextResolver;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.PermissionFeatureHandler;

/**
 * Restricts featured permissions to guidance group students (implied by contextRefenrce) 
 * if the user has role feature FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION_TEST. If the 
 * user doesn't have the feature, returns the default permission.
 */
@PermissionFeature(PyramusPermissionFeatures.ONLY_GROUP_STUDENTS)
@RequestScoped
public class OnlyGroupStudentsPermissionFeature implements PermissionFeatureHandler {

  @Inject
  private Logger logger;
  
  @Inject
  @Any
  private Instance<UserContextResolver> userContextResolvers;
  
  @Inject
  private StudentDAO studentDAO;

  @Inject
  private PermissionController permissionController;
  
  @Override
  public boolean hasPermission(String permission, fi.otavanopisto.security.User user, ContextReference contextReference, boolean allowed) {
    // By default the permission needs to be allowed. This feature only disallows permission.
    if (!allowed)
      return allowed;
    
    User maybeStudent = resolveUser(contextReference);
    
    boolean hf = permissionController.hasEnvironmentPermission(user, StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION);
    
    if (maybeStudent instanceof Student) {
      if (hf && user instanceof StaffMember)
        return studentDAO.isStudyGuider((StaffMember) user, (Student) maybeStudent);
      else
        return allowed;
    } else {
      String contextStr = contextReference == null ? "null" : contextReference.getClass().getSimpleName();
      logger.log(Level.WARNING, String.format("ContextReference %s was not student, ignoring and returning default permission", contextStr));
    }
    
    return allowed;
  }
  
  /**
   * Uses ContextResolvers to resolve user from ContextReference
   * 
   * @param contextReference
   * @return user if found, else null
   */
  protected User resolveUser(ContextReference contextReference) {
    for (UserContextResolver resolver : userContextResolvers) {
      if (resolver.handlesContextReference(contextReference))
        return resolver.resolveUser(contextReference);
    }
    
    return null;
  }
  
}
