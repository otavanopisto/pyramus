package fi.otavanopisto.pyramus.dao.users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth_;

@Stateless
public class InternalAuthDAO extends PyramusEntityDAO<InternalAuth> {

  public InternalAuth create(String username, String password) {
    EntityManager entityManager = getEntityManager();
    
    InternalAuth internalAuth = new InternalAuth();
    internalAuth.setUsername(username);
    internalAuth.setPassword(password);
    
    entityManager.persist(internalAuth);
  
    return internalAuth;
  }
  
  public InternalAuth findByUsernameAndPassword(String username, String passwordEncoded) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<InternalAuth> criteria = criteriaBuilder.createQuery(InternalAuth.class);
    Root<InternalAuth> root = criteria.from(InternalAuth.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(InternalAuth_.username), username),
            criteriaBuilder.equal(root.get(InternalAuth_.password), passwordEncoded)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public InternalAuth update(InternalAuth internalAuth, String username) {
    EntityManager entityManager = getEntityManager();
    internalAuth.setUsername(username);
    entityManager.persist(internalAuth);
    return internalAuth;
  }

  public InternalAuth updateUsername(InternalAuth internalAuth, String username) {
    EntityManager entityManager = getEntityManager();
    internalAuth.setUsername(username);
    entityManager.persist(internalAuth);
    return internalAuth;
  }

  public InternalAuth updatePassword(InternalAuth internalAuth, String password) {
    EntityManager entityManager = getEntityManager();
    internalAuth.setPassword(password);
    entityManager.persist(internalAuth);
    return internalAuth;
  }
  
  public void setInternalAuthPassword(InternalAuth internalAuth, String password) {
    EntityManager entityManager = getEntityManager();
    internalAuth.setPassword(password); 
    entityManager.persist(internalAuth);
  }

  @Override
  public void delete(InternalAuth internalAuth) {
    super.delete(internalAuth);
  }
  
}
