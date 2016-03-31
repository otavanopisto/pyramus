package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.OtherCost;
import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;
import fi.otavanopisto.pyramus.domainmodel.courses.OtherCost_;

@Stateless
public class OtherCostDAO extends PyramusEntityDAO<OtherCost> {

  /**
   * Creates an other cost to the database.
   * 
   * @param course The course the other cost belongs to
   * @param name Other cost name
   * @param cost Other cost value
   * 
   * @return The created other cost
   */
  public OtherCost create(Course course, String name, MonetaryAmount cost) {
    EntityManager entityManager = getEntityManager();

    OtherCost otherCost = new OtherCost(course);
    otherCost.setName(name);
    otherCost.setCost(cost);
    entityManager.persist(otherCost);

    course.getOtherCosts().add(otherCost);
    entityManager.persist(course);
    return otherCost;
  }

  /**
   * Updates an other cost to the database.
   * 
   * @param otherCost The other cost to be updated
   * @param name Other cost name
   * @param cost Other cost value
   */
  public void update(OtherCost otherCost, String name, MonetaryAmount cost) {
    EntityManager entityManager = getEntityManager();
    otherCost.setName(name);
    otherCost.setCost(cost);
    entityManager.persist(otherCost);
  }

  /**
   * Returns a list of all other costs in the course corresponding to the given identifier.
   * 
   * @param courseId The course identifier
   * 
   * @return A list of all other costs in the course corresponding to the given identifier
   */
  public List<OtherCost> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<OtherCost> criteria = criteriaBuilder.createQuery(OtherCost.class);
    Root<OtherCost> root = criteria.from(OtherCost.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(OtherCost_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  /**
   * Deletes the given other cost from the database.
   * 
   * @param otherCost The other cost to be deleted
   */
  @Override
  public void delete(OtherCost otherCost) {
    EntityManager entityManager = getEntityManager();
    Course course = otherCost.getCourse();
    if (course != null) {
      course.removeOtherCost(otherCost);
      entityManager.persist(course);
    }
    entityManager.remove(otherCost);
  }

  
}
