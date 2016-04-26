package fi.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.students.StudentEntity;

public class CourseStudentEntityFactory implements EntityFactory<CourseStudentEntity> {

  public CourseStudentEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;

    CourseStudent courseStudent = (CourseStudent) domainObject;

    StudentEntity student = EntityFactoryVault.buildFromDomainObject(courseStudent.getStudent());
    CourseEntity course = EntityFactoryVault.buildFromDomainObject(courseStudent.getCourse());
    CourseParticipationTypeEntity participationType = EntityFactoryVault.buildFromDomainObject(courseStudent.getParticipationType());
    CourseEnrolmentTypeEntity enrolmentType = EntityFactoryVault.buildFromDomainObject(courseStudent.getCourseEnrolmentType());

    String optionality = null;
    if (courseStudent.getOptionality() != null) {
      optionality = courseStudent.getOptionality().name();
    }

    return new CourseStudentEntity(courseStudent.getId(), courseStudent.getEnrolmentTime(), student, course, participationType, enrolmentType,
        courseStudent.getLodging(), optionality, courseStudent.getArchived());
  }

}
