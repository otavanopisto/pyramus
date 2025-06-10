package fi.otavanopisto.pyramus.dao.grading;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditVariableKey;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditVariableKey_;

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
