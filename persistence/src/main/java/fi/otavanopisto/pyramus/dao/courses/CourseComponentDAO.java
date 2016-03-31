package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseComponent_;

@Stateless
public class CourseComponentDAO extends PyramusEntityDAO<CourseComponent> {
  /**
   * Creates a course component to the database.
   * 
   * @param course The course
   * @param length Component length
   * @param name Component name
   * @param description Component description
   * 
   * @return The created course component
   */
  public CourseComponent create(Course course, Double componentLength,
      EducationalTimeUnit componentLengthTimeUnit, String name, String description) {
    EntityManager entityManager = getEntityManager();

    CourseComponent courseComponent = new CourseComponent();
    courseComponent.setCourse(course);
    courseComponent.setName(name);
    courseComponent.getLength().setUnit(componentLengthTimeUnit);
    courseComponent.getLength().setUnits(componentLength);
    courseComponent.setDescription(description);
    entityManager.persist(courseComponent);

    course.addCourseComponent(courseComponent);
    entityManager.persist(course);

    return courseComponent;
  }

  /**
   * Updates a course component to the database.
   * 
   * @param courseComponent The course component to be updated
   * @param length Component length
   * @param name Component name
   * @param description Component description
   */
  public CourseComponent update(CourseComponent courseComponent, Double length, EducationalTimeUnit lengthTimeUnit,
      String name, String description) {
    EntityManager entityManager = getEntityManager();

    courseComponent.setName(name);
    courseComponent.getLength().setUnit(lengthTimeUnit);
    courseComponent.getLength().setUnits(length);
    courseComponent.setDescription(description);

    entityManager.persist(courseComponent);
    
    return courseComponent;
  }

  /**
   * Deletes the given course component from the database.
   * 
   * @param courseComponent The course component to be deleted
   */
  @Override
  public void delete(CourseComponent courseComponent) {
    EntityManager entityManager = getEntityManager();
    if (courseComponent.getCourse() != null)
      courseComponent.getCourse().removeCourseComponent(courseComponent);
    entityManager.remove(courseComponent);
  }

  public List<CourseComponent> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseComponent> criteria = criteriaBuilder.createQuery(CourseComponent.class);
    Root<CourseComponent> root = criteria.from(CourseComponent.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseComponent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(CourseComponent_.course), course)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
