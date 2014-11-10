package fi.pyramus.services.entities.courses;

import fi.pyramus.domainmodel.courses.CourseStaffMember;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.users.UserEntity;

public class CourseUserEntityFactory implements EntityFactory<CourseUserEntity> {

  public CourseUserEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    CourseStaffMember courseUser = (CourseStaffMember) domainObject;

    CourseEntity course = EntityFactoryVault.buildFromDomainObject(courseUser.getCourse());
    UserEntity user = EntityFactoryVault.buildFromDomainObject(courseUser.getStaffMember());
    CourseUserRoleEntity role = EntityFactoryVault.buildFromDomainObject(courseUser.getRole());

    return new CourseUserEntity(courseUser.getId(), course, user, role);
  }

}
