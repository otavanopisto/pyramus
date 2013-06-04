package fi.pyramus.services.entities.courses;

import fi.pyramus.domainmodel.courses.CourseUser;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.users.UserEntity;

public class CourseUserEntityFactory implements EntityFactory<CourseUserEntity> {

  public CourseUserEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    CourseUser courseUser = (CourseUser) domainObject;

    CourseEntity course = EntityFactoryVault.buildFromDomainObject(courseUser.getCourse());
    UserEntity user = EntityFactoryVault.buildFromDomainObject(courseUser.getUser());
    CourseUserRoleEntity role = EntityFactoryVault.buildFromDomainObject(courseUser.getRole());

    return new CourseUserEntity(courseUser.getId(), course, user, role);
  }

}
