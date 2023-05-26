package fi.otavanopisto.pyramus.security.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.PermissionFeatureHandler;
import fi.otavanopisto.security.PermissionFeatureLiteral;
import fi.otavanopisto.security.User;

@ApplicationScoped
public class EnvironmentPermissionResolver extends AbstractPermissionResolver implements PermissionResolver {

  @Inject
  private Logger logger; 
  
  @Inject
  private EnvironmentRolePermissionDAO environmentUserRolePermissionDAO;

  @Inject
  private CourseStaffMemberDAO courseStaffMemberDAO;

  @Inject
  private CourseStudentDAO courseStudentDAO;
  
  @Inject
  @Any
  private Instance<PermissionFeatureHandler> featureHandlers;
  
  @Override
  public boolean handlesPermission(Permission permission) {
    return permission != null 
        ? PermissionScope.ENVIRONMENT.equals(permission.getScope()) || PermissionScope.COURSE.equals(permission.getScope())
        : false;
  }

  @Override
  public boolean hasPermission(Permission permission, ContextReference contextReference, User user) {
    fi.otavanopisto.pyramus.domainmodel.users.User userEntity = getUser(user);
    
    if (userEntity == null) {
      return hasEveryonePermission(permission, contextReference);
    }
    
    boolean allowed = false;
    Set<Role> environmentRoles = getUserEntityEnvironmentRoles(userEntity);

    if (PermissionScope.COURSE.equals(permission.getScope()) && (contextReference != null)) {
      Course course = resolveCourse(contextReference);
      if (course != null) {
        allowed = hasCourseAccess(course, userEntity, environmentRoles, permission);
      }
    }
    
    allowed = 
        allowed || 
        hasEnvironmentPermissionAccess(environmentRoles, permission) ||
        hasEveryonePermission(permission, contextReference);

    PyramusPermissionCollection collection = findCollection(permission.getName());
    try {
      PermissionFeature[] features = collection.listPermissionFeatures(permission.getName());
      if (features != null) {
        for (PermissionFeature feature : features) {
          Instance<PermissionFeatureHandler> instance = featureHandlers.select(new PermissionFeatureLiteral(feature.value()));
          if (!instance.isUnsatisfied()) {
            PermissionFeatureHandler permissionFeatureHandler = instance.get();
            allowed = permissionFeatureHandler.hasPermission(permission.getName(), userEntity, contextReference, allowed);
          } else
            logger.log(Level.SEVERE, String.format("Unsatisfied permission feature %s", feature.value()));
        }
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, String.format("Could not list permission features for permission %s", permission), e);
    }
    
    return allowed;
  }

  private Set<Role> getUserEntityEnvironmentRoles(fi.otavanopisto.pyramus.domainmodel.users.User userEntity) {
    return userEntity.getRoles() != null ? Set.copyOf(userEntity.getRoles()) : Collections.emptySet();
  }

  private boolean hasEnvironmentPermissionAccess(Collection<Role> environmentRoles, Permission permission) {
    return CollectionUtils.isNotEmpty(environmentUserRolePermissionDAO.listByUserRolesAndPermission(environmentRoles, permission));
  }
  
  private boolean hasCourseAccess(Course course, fi.otavanopisto.pyramus.domainmodel.users.User userEntity,
      Collection<Role> userEnvironmentRoles, Permission permission) {
    PyramusPermissionCollection permissionCollection = findCollection(permission.getName());
    if (permissionCollection != null) {
      try {
        String[] defaultRoles = permissionCollection.getDefaultRoles(permission.getName());

        for (Role userEnvironmentRole : userEnvironmentRoles) {
          // Is EnvironmentRole in the environment roles of the permission
          if ((userEnvironmentRole != null) && ArrayUtils.contains(defaultRoles, userEnvironmentRole.toString())) {
            return true;
          }
        }
        
        CourseRoleArchetype[] defaultCourseRoles = permissionCollection.getDefaultCourseRoles(permission.getName());
        if (userEntity instanceof Student) {
          CourseStudent courseStudent = courseStudentDAO.findByCourseAndStudent(course, (Student) userEntity);
          if (courseStudent != null) {
            return ArrayUtils.contains(defaultCourseRoles, CourseRoleArchetype.STUDENT);
          } else {
            return false;
          }
        } 
        else if (userEntity instanceof StaffMember) {
          CourseStaffMember courseStaffMember = courseStaffMemberDAO.findByCourseAndStaffMember(course, (StaffMember) userEntity);
          // There may be several types of Roles but they don't have a particular type so we just default to a generic course staff type
          
          if (courseStaffMember != null) {
            return ArrayUtils.contains(defaultCourseRoles, CourseRoleArchetype.TEACHER);
          } else {
            return false;
          }
        } else {
          logger.severe(String.format("UserEntity could not be casted to a student nor staffmember."));
        }
      } catch (NoSuchFieldException e) {
      }
    }

    return false;
  }

  @Override
  public boolean hasEveryonePermission(Permission permission, ContextReference contextReference) {
    Role everyoneRole = getEveryoneRole();
    return hasEnvironmentPermissionAccess(Arrays.asList(everyoneRole), permission);
  }

}
