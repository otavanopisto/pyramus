package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseComponentResource;
import fi.pyramus.domainmodel.courses.CourseComponentResource_;
import fi.pyramus.domainmodel.resources.Resource;

@Stateless
public class CourseComponentResourceDAO extends PyramusEntityDAO<CourseComponentResource> {

  public List<CourseComponentResource> listByCourseComponent(CourseComponent courseComponent) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseComponentResource> criteria = criteriaBuilder.createQuery(CourseComponentResource.class);
    Root<CourseComponentResource> root = criteria.from(CourseComponentResource.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseComponentResource_.courseComponent), courseComponent)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public CourseComponentResource create(CourseComponent courseComponent, Resource resource, Double usagePercent) {
    EntityManager entityManager = getEntityManager();
    
    CourseComponentResource courseComponentResource = new CourseComponentResource();
    courseComponentResource.setResource(resource);
    courseComponentResource.setUsagePercent(usagePercent);
    
    entityManager.persist(courseComponentResource);
    
    courseComponent.addResource(courseComponentResource);
    entityManager.persist(courseComponent);
    
    return courseComponentResource;
  }
  
  public CourseComponentResource updateUsagePercent(CourseComponentResource courseComponentResource, Double usagePercent) {
    EntityManager entityManager = getEntityManager();
    
    courseComponentResource.setUsagePercent(usagePercent);
    
    entityManager.persist(courseComponentResource);
    
    return courseComponentResource;
  }
  
  @Override
  public void delete(CourseComponentResource courseComponentResource) {
    EntityManager entityManager = getEntityManager();
    
    CourseComponent courseComponent = courseComponentResource.getCourseComponent();
    courseComponent.removeResource(courseComponentResource);
    entityManager.persist(courseComponent);
    
    entityManager.remove(courseComponentResource);
  }
  

}
