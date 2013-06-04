package fi.pyramus.dao.users;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.users.UserVariableKey;
import fi.pyramus.domainmodel.users.UserVariableKey_;

@Stateless
public class UserVariableKeyDAO extends PyramusEntityDAO<UserVariableKey> {

  /**
   * Returns a list of user editable user variable keys from the database, sorted by their user interface name.
   * 
   * @return A list of user editable uservariable keys
   */
  public List<UserVariableKey> listUserEditableUserVariableKeys() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserVariableKey> criteria = criteriaBuilder.createQuery(UserVariableKey.class);
    Root<UserVariableKey> root = criteria.from(UserVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(UserVariableKey_.userEditable), Boolean.TRUE)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public UserVariableKey findByVariableKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserVariableKey> criteria = criteriaBuilder.createQuery(UserVariableKey.class);
    Root<UserVariableKey> root = criteria.from(UserVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(UserVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  
}
