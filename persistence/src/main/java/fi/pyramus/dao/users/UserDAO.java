package fi.pyramus.dao.users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactInfo_;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Email_;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.User_;

@Stateless
public class UserDAO extends PyramusEntityDAO<User> {

  public User findByExternalIdAndAuthProvider(String externalId, String authProvider) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(User_.externalId), externalId),
            criteriaBuilder.equal(root.get(User_.authProvider), authProvider)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public User findByEmail(String email) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    
    Join<User, ContactInfo> contactInfoJoin = root.join(User_.contactInfo);
    ListJoin<ContactInfo, Email> emailJoin = contactInfoJoin.join(ContactInfo_.emails);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(emailJoin.get(Email_.address), email)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public User updateAuthProvider(User user, String authProvider) {
    user.setAuthProvider(authProvider);
    persist(user);
    return user;
  }

  public User updateExternalId(User user, String externalId) {
    user.setExternalId(externalId);
    persist(user);
    return user;
  }
  
}
