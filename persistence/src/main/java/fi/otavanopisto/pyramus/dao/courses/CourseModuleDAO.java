package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule_;
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

  public List<CourseModule> listByCourse(CourseBase courseBase) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseModule> criteria = criteriaBuilder.createQuery(CourseModule.class);
    Root<CourseModule> root = criteria.from(CourseModule.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseModule_.course), courseBase)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CourseModule> listByCourseAndSubject(CourseBase courseBase, Subject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseModule> criteria = criteriaBuilder.createQuery(CourseModule.class);
    Root<CourseModule> root = criteria.from(CourseModule.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseModule_.course), courseBase),
            criteriaBuilder.equal(root.get(CourseModule_.subject), subject)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(CourseModule courseModule) {
    if (courseModule.getCourse() != null) {
      courseModule.getCourse().removeCourseModule(courseModule);
    }
    
    super.delete(courseModule);
  }
  
}
