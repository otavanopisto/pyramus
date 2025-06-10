package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType_;

@Stateless
public class CourseTypeDAO extends PyramusEntityDAO<CourseType> {

  public CourseType create(String name) {
    return create(name, Boolean.FALSE);
  }
  
  public CourseType create(String name, Boolean archived) {
    CourseType courseState = new CourseType();
    courseState.setName(name);
    courseState.setArchived(archived);
    
    return persist(courseState);
  }

  public List<CourseType> listByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseType> criteria = criteriaBuilder.createQuery(CourseType.class);
    Root<CourseType> root = criteria.from(CourseType.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseType_.name), name)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public CourseType updateName(CourseType courseType, String name) {
    courseType.setName(name);
    return persist(courseType);
  }

}
