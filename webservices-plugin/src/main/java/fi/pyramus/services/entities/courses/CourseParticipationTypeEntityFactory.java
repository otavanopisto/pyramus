package fi.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.services.entities.EntityFactory;

public class CourseParticipationTypeEntityFactory implements EntityFactory<CourseParticipationTypeEntity> {

  public CourseParticipationTypeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;

    CourseParticipationType courseEnrolmentType = (CourseParticipationType) domainObject;
    return new CourseParticipationTypeEntity(courseEnrolmentType.getId(), courseEnrolmentType.getName());
  }

}
