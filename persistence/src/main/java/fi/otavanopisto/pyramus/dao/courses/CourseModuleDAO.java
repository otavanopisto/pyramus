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
    entityManager.remove(courseModule);
  }

//  public List<CourseModule> listByCourse(Course course) {
//    EntityManager entityManager = getEntityManager(); 
//    
//    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//    CriteriaQuery<CourseModule> criteria = criteriaBuilder.createQuery(CourseModule.class);
//    Root<CourseModule> root = criteria.from(CourseModule.class);
//    criteria.select(root);
//    criteria.where(
//        criteriaBuilder.and(
//            criteriaBuilder.equal(root.get(CourseModule_.archived), Boolean.FALSE),
//            criteriaBuilder.equal(root.get(CourseModule_.course), course)
//        ));
//    
//    return entityManager.createQuery(criteria).getResultList();
//  }

}
