package fi.pyramus.services.entities.courses;

import fi.pyramus.domainmodel.courses.CourseUserRole;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.courses.CourseUserRoleEntity;

public class CourseUserRoleEntityFactory implements EntityFactory<CourseUserRoleEntity> {

  public CourseUserRoleEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    CourseUserRole courseUserRole = (CourseUserRole) domainObject;
    return new CourseUserRoleEntity(courseUserRole.getId(), courseUserRole.getName());
  }

}
