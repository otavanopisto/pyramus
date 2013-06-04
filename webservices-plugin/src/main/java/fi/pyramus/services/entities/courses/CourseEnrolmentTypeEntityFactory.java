package fi.pyramus.services.entities.courses;

import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.services.entities.EntityFactory;

public class CourseEnrolmentTypeEntityFactory implements EntityFactory<CourseEnrolmentTypeEntity> {

  public CourseEnrolmentTypeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;

    CourseEnrolmentType courseEnrolmentType = (CourseEnrolmentType) domainObject;
    return new CourseEnrolmentTypeEntity(courseEnrolmentType.getId(), courseEnrolmentType.getName());
  }

}
