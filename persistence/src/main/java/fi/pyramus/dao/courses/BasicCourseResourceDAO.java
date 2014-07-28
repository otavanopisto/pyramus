package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.BasicCourseResource;
import fi.pyramus.domainmodel.courses.BasicCourseResource_;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.persistence.usertypes.MonetaryAmount;

@Stateless
public class BasicCourseResourceDAO extends PyramusEntityDAO<BasicCourseResource> {
  /**
   * Creates a basic course resource to the database.
   * 
   * @param course The course
   * @param resource The resource
   * @param hours Resource hours
   * @param hourlyCost Resource hourly cost
   * @param units Resource units
   * @param unitCost Resource unit cost
   * 
   * @return The created basic course resource
   */
  public BasicCourseResource create(Course course, Resource resource, Double hours,
      MonetaryAmount hourlyCost, Integer units, MonetaryAmount unitCost) {
    EntityManager entityManager = getEntityManager();

    BasicCourseResource basicCourseResource = new BasicCourseResource(course, resource);
    basicCourseResource.setHours(hours);
    basicCourseResource.setHourlyCost(hourlyCost);
    basicCourseResource.setUnits(units);
    basicCourseResource.setUnitCost(unitCost);
    entityManager.persist(basicCourseResource);

    course.getBasicCourseResources().add(basicCourseResource);
    entityManager.persist(course);
    return basicCourseResource;
  }

  /**
   * Updates the given basic course resource to the database.
   * 
   * @param basicCourseResource The basic course resource to be updated
   * @param hours Resource hours
   * @param hourlyCost Resource hourly cost
   * @param units Resource units
   * @param unitCost Resource unit cost
   */
  public void update(BasicCourseResource basicCourseResource, Double hours,
      MonetaryAmount hourlyCost, Integer units, MonetaryAmount unitCost) {
    EntityManager entityManager = getEntityManager();
    basicCourseResource.setHours(hours);
    basicCourseResource.setHourlyCost(hourlyCost);
    basicCourseResource.setUnits(units);
    basicCourseResource.setUnitCost(unitCost);
    entityManager.persist(basicCourseResource);
  }

  /**
   * Returns a list of all basic course resources in the course corresponding to the given
   * identifier.
   * 
   * @param courseId The course identifier
   * 
   * @return A list of all basic course resources in the course corresponding to the given
   * identifier
   */
  public List<BasicCourseResource> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<BasicCourseResource> criteria = criteriaBuilder.createQuery(BasicCourseResource.class);
    Root<BasicCourseResource> root = criteria.from(BasicCourseResource.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(BasicCourseResource_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  /**
   * Deletes the given basic course resource from the database.
   * 
   * @param basicCourseResource The basic course resource to be deleted
   */
  @Override
  public void delete(BasicCourseResource basicCourseResource) {
    EntityManager entityManager = getEntityManager(); 
    if (basicCourseResource.getCourse() != null)
      basicCourseResource.getCourse().removeBasicCourseResource(basicCourseResource);
    entityManager.remove(basicCourseResource);
  }

}
