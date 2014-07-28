package fi.pyramus.dao.grading;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.grading.CreditVariableKey;
import fi.pyramus.domainmodel.grading.CreditVariableKey_;

@Stateless
public class CreditVariableKeyDAO extends PyramusEntityDAO<CreditVariableKey> {

  public CreditVariableKey create(boolean userEditable, String variableKey, String variableName, VariableType variableType) {
    EntityManager entityManager = getEntityManager();
      
    CreditVariableKey creditVariableKey = new CreditVariableKey();
    creditVariableKey.setUserEditable(userEditable);
    creditVariableKey.setVariableKey(variableKey);
    creditVariableKey.setVariableName(variableName);
    creditVariableKey.setVariableType(variableType);
    entityManager.persist(creditVariableKey);
    return creditVariableKey;
  }
  
  public CreditVariableKey findByKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditVariableKey> criteria = criteriaBuilder.createQuery(CreditVariableKey.class);
    Root<CreditVariableKey> root = criteria.from(CreditVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CreditVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Returns a list of user editable student variable keys from the database, sorted by their user interface name.
   * 
   * @return A list of user editable student variable keys
   */
  public List<CreditVariableKey> listUserEditableCreditVariableKeys() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditVariableKey> criteria = criteriaBuilder.createQuery(CreditVariableKey.class);
    Root<CreditVariableKey> root = criteria.from(CreditVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CreditVariableKey_.userEditable), Boolean.TRUE)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
