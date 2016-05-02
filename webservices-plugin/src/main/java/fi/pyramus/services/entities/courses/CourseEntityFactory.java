package fi.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.base.SubjectEntity;
import fi.pyramus.services.entities.users.UserEntity;

public class CourseEntityFactory implements EntityFactory<CourseEntity> {

  public CourseEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;

    Course course = (Course) domainObject;

    UserEntity creator = EntityFactoryVault.buildFromDomainObject((StaffMember) course.getCreator());
    UserEntity lastModifier = EntityFactoryVault.buildFromDomainObject((StaffMember) course.getLastModifier());
    SubjectEntity subject = EntityFactoryVault.buildFromDomainObject(course.getSubject());
    CourseComponentEntity[] components = (CourseComponentEntity[]) EntityFactoryVault.buildFromDomainObjects(course.getCourseComponents());
    CourseEducationTypeEntity[] educationTypes = (CourseEducationTypeEntity[]) EntityFactoryVault.buildFromDomainObjects(course.getCourseEducationTypes());
    Double courseLengthUnits = null;
    Long courseLengthUnitId = null;
    if (course.getCourseLength() != null) {
      courseLengthUnits = course.getCourseLength().getUnits();
      if (course.getCourseLength().getUnit() != null)
        courseLengthUnitId = course.getCourseLength().getUnit().getId();
    }
    
    int i = 0;
    String[] tags = new String[course.getTags().size()];
    for (Tag tag : course.getTags()) {
      tags[i++] = tag.getText();
    }
    
    return new CourseEntity(course.getId(), course.getName(), course.getNameExtension(), tags, creator, course.getCreated(), lastModifier, course.getLastModified(), course.getDescription(),
        subject, course.getCourseNumber(), courseLengthUnits, courseLengthUnitId, educationTypes, course.getArchived(), components, course
            .getModule().getId(), course.getBeginDate(), course.getEndDate());
  }
}
