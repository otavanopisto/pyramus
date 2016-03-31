package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseUser;
import fi.pyramus.domainmodel.courses.CourseUser_;

@Stateless
public class CourseUserDAO extends PyramusEntityDAO<CourseUser> {

  public List<CourseUser> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseUser> criteria = criteriaBuilder.createQuery(CourseUser.class);
    Root<CourseUser> root = criteria.from(CourseUser.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseUser_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
