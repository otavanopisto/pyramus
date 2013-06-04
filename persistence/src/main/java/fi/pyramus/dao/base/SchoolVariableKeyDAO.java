package fi.pyramus.dao.base;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.domainmodel.base.SchoolVariableKey_;

@Stateless
public class SchoolVariableKeyDAO extends PyramusEntityDAO<SchoolVariableKey> {

  public SchoolVariableKey findVariableKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SchoolVariableKey> criteria = criteriaBuilder.createQuery(SchoolVariableKey.class);
    Root<SchoolVariableKey> root = criteria.from(SchoolVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(SchoolVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Returns a list of user editable school variable keys from the database, sorted by their user interface name.
   * 
   * @return A list of user editable school variable keys
   */
  public List<SchoolVariableKey> listUserEditableVariableKeys() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SchoolVariableKey> criteria = criteriaBuilder.createQuery(SchoolVariableKey.class);
    Root<SchoolVariableKey> root = criteria.from(SchoolVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(SchoolVariableKey_.userEditable), Boolean.TRUE)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
