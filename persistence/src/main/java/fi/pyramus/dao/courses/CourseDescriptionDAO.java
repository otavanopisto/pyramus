package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.CourseBase;
import fi.pyramus.domainmodel.courses.CourseDescription;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory_;
import fi.pyramus.domainmodel.courses.CourseDescription_;

@Stateless
public class CourseDescriptionDAO extends PyramusEntityDAO<CourseDescription> {
  public CourseDescription create(CourseBase courseBase, CourseDescriptionCategory category, String description) {
    EntityManager entityManager = getEntityManager();
    
    CourseDescription courseDescription = new CourseDescription();
    courseDescription.setCourseBase(courseBase);
    courseDescription.setCategory(category);
    courseDescription.setDescription(description);
    
    entityManager.persist(courseDescription);

    return courseDescription;
  }
  
  public CourseDescription update(CourseDescription courseDescription, CourseBase courseBase, CourseDescriptionCategory category, String description) {
    EntityManager entityManager = getEntityManager();
    
    courseDescription.setCourseBase(courseBase);
    courseDescription.setCategory(category);
    courseDescription.setDescription(description);
    
    entityManager.persist(courseDescription);

    return courseDescription;
  }

  public List<CourseDescription> listByCourseBase(CourseBase courseBase) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseDescription> criteria = criteriaBuilder.createQuery(CourseDescription.class);
    Root<CourseDescription> root = criteria.from(CourseDescription.class);
    Join<CourseDescription, CourseDescriptionCategory> category = root.join(CourseDescription_.category);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseDescription_.courseBase), courseBase),
            criteriaBuilder.equal(category.get(CourseDescriptionCategory_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public CourseDescription findByCourseAndCategory(CourseBase courseBase, CourseDescriptionCategory category) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseDescription> criteria = criteriaBuilder.createQuery(CourseDescription.class);
    Root<CourseDescription> root = criteria.from(CourseDescription.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseDescription_.courseBase), courseBase),
            criteriaBuilder.equal(root.get(CourseDescription_.category), category)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public void copy(CourseBase fromCourseBase, CourseBase toCourseBase) {
    List<CourseDescription> descriptions = listByCourseBase(fromCourseBase);
    
    for (CourseDescription desc : descriptions) {
      CourseDescription existingDescription = findByCourseAndCategory(toCourseBase, desc.getCategory());

      if (existingDescription == null)
        create(toCourseBase, desc.getCategory(), desc.getDescription());
      else
        update(existingDescription, toCourseBase, desc.getCategory(), desc.getDescription());
    }
  }
  
  @Override
  public void delete(CourseDescription courseDescription) {
    super.delete(courseDescription);
  }
}
