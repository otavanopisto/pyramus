package fi.pyramus.dao.users;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.users.UserVariableKey;
import fi.pyramus.domainmodel.users.UserVariableKey_;

@Stateless
public class UserVariableKeyDAO extends PyramusEntityDAO<UserVariableKey> {
  
  public UserVariableKey create(String variableKey, String variableName, VariableType variableType, Boolean userEditable) {
    UserVariableKey userVariableKey = new UserVariableKey();
    
    userVariableKey.setUserEditable(userEditable);
    userVariableKey.setVariableKey(variableKey);
    userVariableKey.setVariableName(variableName);
    userVariableKey.setVariableType(variableType);
    
    return persist(userVariableKey);
  }
  
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

  public List<UserVariableKey> listByUserEditable(Boolean userEditable) {
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

  public UserVariableKey updateVariableName(UserVariableKey userVariableKey, String variableName) {
    userVariableKey.setVariableName(variableName);
    return persist(userVariableKey);
  }

  public UserVariableKey updateVariableType(UserVariableKey userVariableKey, VariableType variableType) {
    userVariableKey.setVariableType(variableType);
    return persist(userVariableKey);
  }

  public UserVariableKey updateUserEditable(UserVariableKey userVariableKey, Boolean userEditable) {
    userVariableKey.setUserEditable(userEditable);
    return persist(userVariableKey);
  }

  
}
