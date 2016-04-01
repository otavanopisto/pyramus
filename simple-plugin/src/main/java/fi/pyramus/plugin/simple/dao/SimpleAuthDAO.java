package fi.otavanopisto.pyramus.plugin.simple.dao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.plugin.simple.domainmodel.users.SimpleAuth;
import fi.otavanopisto.pyramus.plugin.simple.domainmodel.users.SimpleAuth_;

/** A data access object for the SimplePlugin authorization provider.
 * 
 */
public class SimpleAuthDAO extends PyramusEntityDAO<SimpleAuth> {

  /** Create a new persistent <code>SimpleAuth</code> object.
   * 
   * @param username The username of the new user.
   * @param password The password of the new user.
   * @return The new <code>SimpleAuth</code> object.
   */
  public SimpleAuth create(String username, String password) {
    EntityManager entityManager = getEntityManager();
    
    SimpleAuth simpleAuth = new SimpleAuth();
    simpleAuth.setUsername(username);
    simpleAuth.setPassword(password);
    
    entityManager.persist(simpleAuth);
    
    return simpleAuth;
  }

  /** Find a SimpleAuth object with specific username and password.
   * 
   * @param username The username of the user to find.
   * @param password The password of the user to find.
   * @return The <code>SimpleAuth</code> object with the specified username and password,
   * or <code>null</code> if such object was not found. 
   */
  public SimpleAuth findByUserNameAndPassword(String username, String password) {
    EntityManager entityManager = getEntityManager();
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SimpleAuth> criteria = criteriaBuilder.createQuery(SimpleAuth.class);
    Root<SimpleAuth> root = criteria.from(SimpleAuth.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(SimpleAuth_.username), username),
            criteriaBuilder.equal(root.get(SimpleAuth_.password), password)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  /** Update the username of a <code>SimpleAuth</code> object.
   * 
   * @param simpleAuth The object to update.
   * @param username The new username.
   */
  public void updateUsername(SimpleAuth simpleAuth, String username) {
    EntityManager entityManager = getEntityManager();
    
    simpleAuth.setUsername(username);
    
    entityManager.persist(simpleAuth);
  }
  
  /** Update the password of a <code>SimpleAuth</code> object.
   * 
   * @param simpleAuth The object to update.
   * @param password The new password.
   */
  public void updatePassword(SimpleAuth simpleAuth, String password) {
    EntityManager entityManager = getEntityManager();
    
    simpleAuth.setPassword(password);
    
    entityManager.persist(simpleAuth);
  }
  
  @Override
  public void delete(SimpleAuth simpleAuth) {
    super.delete(simpleAuth);
  }
}
