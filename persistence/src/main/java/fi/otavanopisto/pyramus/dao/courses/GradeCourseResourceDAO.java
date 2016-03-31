package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.GradeCourseResource;
import fi.otavanopisto.pyramus.domainmodel.resources.Resource;
import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;
import fi.pyramus.domainmodel.courses.GradeCourseResource_;

@Stateless
public class GradeCourseResourceDAO extends PyramusEntityDAO<GradeCourseResource> {
  /**
   * Creates a grade course resource to the database.
   * 
   * @param course The course
   * @param resource The resource
   * @param hours Resource hours
   * @param hourlyCost Resource hourly cost
   * @param unitCost Resource unit cost
   * 
   * @return The created grade course resource
   */
  public GradeCourseResource create(Course course, Resource resource, Double hours,
      MonetaryAmount hourlyCost, MonetaryAmount unitCost) {
    EntityManager entityManager = getEntityManager();

    GradeCourseResource gradeCourseResource = new GradeCourseResource(course, resource);
    gradeCourseResource.setHours(hours);
    gradeCourseResource.setHourlyCost(hourlyCost);
    gradeCourseResource.setUnitCost(unitCost);
    entityManager.persist(gradeCourseResource);

    course.getGradeCourseResources().add(gradeCourseResource);
    entityManager.persist(course);
    return gradeCourseResource;
  }

  /**
   * Updates the given grade course resource to the database.
   * 
   * @param gradeCourseResource The grade course resource to be updated
   * @param hours Resource hours
   * @param hourlyCost Resource hourly cost
   * @param unitCost Resource unit cost
   */
  public void update(GradeCourseResource gradeCourseResource, Double hours,
      MonetaryAmount hourlyCost, MonetaryAmount unitCost) {
    EntityManager entityManager = getEntityManager();
    gradeCourseResource.setHours(hours);
    gradeCourseResource.setHourlyCost(hourlyCost);
    gradeCourseResource.setUnitCost(unitCost);
    entityManager.persist(gradeCourseResource);
  }

  public List<GradeCourseResource> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<GradeCourseResource> criteria = criteriaBuilder.createQuery(GradeCourseResource.class);
    Root<GradeCourseResource> root = criteria.from(GradeCourseResource.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(GradeCourseResource_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  /**
   * Deletes the given grade course resource from the database.
   * 
   * @param gradeCourseResource The grade course resource to be deleted
   */
  @Override
  public void delete(GradeCourseResource gradeCourseResource) {
    EntityManager entityManager = getEntityManager();
    if (gradeCourseResource.getCourse() != null)
      gradeCourseResource.getCourse().removeGradeCourseResource(gradeCourseResource);
    entityManager.remove(gradeCourseResource);
  }


}
