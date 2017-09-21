package fi.otavanopisto.pyramus.dao.users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.User_;

@Stateless
public class UserDAO extends PyramusEntityDAO<User> {
  
  public User findByContactInfo(ContactInfo contactInfo) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(User_.contactInfo), contactInfo)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
}
