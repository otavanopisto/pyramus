package fi.otavanopisto.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseUserRoleEntity;

public class CourseUserRoleEntityFactory implements EntityFactory<CourseUserRoleEntity> {

  public CourseUserRoleEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    
    CourseStaffMemberRole courseStaffMemberRole = (CourseStaffMemberRole) domainObject;
    return new CourseUserRoleEntity(courseStaffMemberRole.getId(), courseStaffMemberRole.getName()); 
  }

}
