package fi.otavanopisto.pyramus.dao.courses;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;

@Stateless
public class CourseModuleDAO extends PyramusEntityDAO<CourseModule> {

  public CourseModule create(CourseBase course, Subject subject, Integer courseNumber, Double courseLength,
      EducationalTimeUnit courseLengthTimeUnit) {
    EntityManager entityManager = getEntityManager();

    EducationalLength educationalLength = new EducationalLength();
    educationalLength.setUnits(courseLength);
    educationalLength.setUnit(courseLengthTimeUnit);
    
    CourseModule courseModule = new CourseModule();
    courseModule.setCourse(course);
    courseModule.setSubject(subject);
    courseModule.setCourseNumber(courseNumber);
    courseModule.setCourseLength(educationalLength);
    entityManager.persist(courseModule);

    course.addCourseModule(courseModule);
    entityManager.persist(course);
    
    return courseModule;
  }

  public CourseModule update(CourseModule courseModule, Subject subject, Integer courseNumber, 
      Double courseLength, EducationalTimeUnit courseLengthTimeUnit) {
    EntityManager entityManager = getEntityManager();

    EducationalLength educationalLength = courseModule.getCourseLength();
    if (educationalLength == null) {
      educationalLength = new EducationalLength();
    }
    educationalLength.setUnits(courseLength);
    educationalLength.setUnit(courseLengthTimeUnit);

    courseModule.setSubject(subject);
    courseModule.setCourseNumber(courseNumber);
    courseModule.setCourseLength(educationalLength);

    entityManager.persist(courseModule);
    
    return courseModule;
  }

  /**
   * Deletes the given course component from the database.
   * 
   * @param courseComponent The course component to be deleted
   */
  @Override
  public void delete(CourseModule courseModule) {
    EntityManager entityManager = getEntityManager();
    
    if (courseModule.getCourse() != null && courseModule.getCourse().getCourseModules().contains(courseModule)) {
      courseModule.getCourse().getCourseModules().remove(courseModule);
      entityManager.persist(courseModule.getCourse());
    }
    
    entityManager.remove(courseModule);
  }

}
