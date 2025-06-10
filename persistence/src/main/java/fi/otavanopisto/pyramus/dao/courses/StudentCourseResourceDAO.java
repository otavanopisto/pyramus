package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.StudentCourseResource;
import fi.otavanopisto.pyramus.domainmodel.resources.Resource;
import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;
import fi.otavanopisto.pyramus.domainmodel.courses.StudentCourseResource_;

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
