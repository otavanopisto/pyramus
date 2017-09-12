package fi.otavanopisto.pyramus.dao.users;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariableKey;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariableKey_;

@Stateless
public class PersonVariableKeyDAO extends PyramusEntityDAO<PersonVariableKey> {
  
  public PersonVariableKey create(String variableKey, String variableName, VariableType variableType, Boolean userEditable) {
    PersonVariableKey personVariableKey = new PersonVariableKey();
    
    personVariableKey.setUserEditable(userEditable);
    personVariableKey.setVariableKey(variableKey);
    personVariableKey.setVariableName(variableName);
    personVariableKey.setVariableType(variableType);
    
    return persist(personVariableKey);
  }
  
  public List<PersonVariableKey> listUserEditablePersonVariableKeys() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PersonVariableKey> criteria = criteriaBuilder.createQuery(PersonVariableKey.class);
    Root<PersonVariableKey> root = criteria.from(PersonVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(PersonVariableKey_.userEditable), Boolean.TRUE)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public PersonVariableKey findByVariableKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PersonVariableKey> criteria = criteriaBuilder.createQuery(PersonVariableKey.class);
    Root<PersonVariableKey> root = criteria.from(PersonVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(PersonVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public PersonVariableKey updateVariableName(PersonVariableKey variableKey, String variableName) {
    variableKey.setVariableName(variableName);
    return persist(variableKey);
  }

  public PersonVariableKey updateVariableType(PersonVariableKey variableKey, VariableType variableType) {
    variableKey.setVariableType(variableType);
    return persist(variableKey);
  }

  public PersonVariableKey updateUserEditable(PersonVariableKey variableKey, Boolean userEditable) {
    variableKey.setUserEditable(userEditable);
    return persist(variableKey);
  }
  
}
