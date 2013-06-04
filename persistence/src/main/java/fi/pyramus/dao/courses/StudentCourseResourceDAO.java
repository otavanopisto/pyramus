package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.StudentCourseResource;
import fi.pyramus.domainmodel.courses.StudentCourseResource_;
import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.persistence.usertypes.MonetaryAmount;

@Stateless
public class StudentCourseResourceDAO extends PyramusEntityDAO<StudentCourseResource> {
  /**
   * Creates a student course resource to the database.
   * 
   * @param course The course
   * @param resource The resource
   * @param hours Resource hours
   * @param hourlyCost Resource hourly cost
   * @param unitCost Resource unit cost
   * 
   * @return The created student course resource
   */
  public StudentCourseResource create(Course course, Resource resource, Double hours,
      MonetaryAmount hourlyCost, MonetaryAmount unitCost) {
    EntityManager entityManager = getEntityManager();

    StudentCourseResource studentCourseResource = new StudentCourseResource(course, resource);
    studentCourseResource.setHours(hours);
    studentCourseResource.setHourlyCost(hourlyCost);
    studentCourseResource.setUnitCost(unitCost);
    entityManager.persist(studentCourseResource);

    course.getStudentCourseResources().add(studentCourseResource);
    entityManager.persist(course);
    return studentCourseResource;
  }

  /**
   * Updates the given student course resource to the database.
   * 
   * @param studentCourseResource The student course resource to be updated
   * @param hours Resource hours
   * @param hourlyCost Resource hourly cost
   * @param unitCost Resource unit cost
   */
  public void update(StudentCourseResource studentCourseResource, Double hours,
      MonetaryAmount hourlyCost, MonetaryAmount unitCost) {
    EntityManager entityManager = getEntityManager();

    studentCourseResource.setHours(hours);
    studentCourseResource.setHourlyCost(hourlyCost);
    studentCourseResource.setUnitCost(unitCost);
    entityManager.persist(studentCourseResource);
  }

  public List<StudentCourseResource> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentCourseResource> criteria = criteriaBuilder.createQuery(StudentCourseResource.class);
    Root<StudentCourseResource> root = criteria.from(StudentCourseResource.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentCourseResource_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  /**
   * Deletes the given student course resource from the database.
   * 
   * @param studentCourseResource The student course resource to be deleted
   */
  @Override
  public void delete(StudentCourseResource studentCourseResource) {
    EntityManager entityManager = getEntityManager();
    if (studentCourseResource.getCourse() != null)
      studentCourseResource.getCourse().removeStudentCourseResource(studentCourseResource);
    entityManager.remove(studentCourseResource);
  }


}
