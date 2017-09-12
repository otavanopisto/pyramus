package fi.otavanopisto.pyramus.dao.users;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariable;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariableKey;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariableKey_;
import fi.otavanopisto.pyramus.domainmodel.users.PersonVariable_;

@Stateless
public class PersonVariableDAO extends PyramusEntityDAO<PersonVariable> {

  public PersonVariable create(Person person, PersonVariableKey key, String value) {
    PersonVariable personVariable = new PersonVariable();
    personVariable.setPerson(person);
    personVariable.setKey(key);
    personVariable.setValue(value);
    
    return persist(personVariable);
  }
  
  public PersonVariable findByPersonAndVariableKey(Person person, PersonVariableKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PersonVariable> criteria = criteriaBuilder.createQuery(PersonVariable.class);
    Root<PersonVariable> root = criteria.from(PersonVariable.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(PersonVariable_.person), person),
            criteriaBuilder.equal(root.get(PersonVariable_.key), key)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public String findByPersonAndKey(Person person, String key) {
    PersonVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getPersonVariableKeyDAO();
    
    PersonVariableKey personVariableKey = variableKeyDAO.findByVariableKey(key);
    if (personVariableKey != null) {
      PersonVariable personVariable = findByPersonAndVariableKey(person, personVariableKey); 
      return personVariable == null ? null : personVariable.getValue();
    }
    else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  public List<PersonVariable> listByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PersonVariable> criteria = criteriaBuilder.createQuery(PersonVariable.class);
    Root<PersonVariable> root = criteria.from(PersonVariable.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(PersonVariable_.person), person)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<PersonVariable> listByPersonAndUserEditable(Person person, Boolean userEditable) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PersonVariable> criteria = criteriaBuilder.createQuery(PersonVariable.class);
    Root<PersonVariable> root = criteria.from(PersonVariable.class);
    Join<PersonVariable, PersonVariableKey> key = root.join(PersonVariable_.key);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(PersonVariable_.person), person),
            criteriaBuilder.equal(key.get(PersonVariableKey_.userEditable), userEditable)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public void setPersonVariable(Person person, String key, String value) {
    PersonVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getPersonVariableKeyDAO();
    
    PersonVariableKey personVariableKey = variableKeyDAO.findByVariableKey(key);
    if (personVariableKey != null) {
      PersonVariable personVariable = findByPersonAndVariableKey(person, personVariableKey);
      if (StringUtils.isBlank(value)) {
        if (personVariable != null) {
          delete(personVariable);
        }
      }
      else {
        if (personVariable == null) {
          personVariable = create(person, personVariableKey, value);
        }
        else {
          updateValue(personVariable, value);
        }
      }
    }
    else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }
  
  public PersonVariable updateValue(PersonVariable personVariable, String value) {
    personVariable.setValue(value);
    return persist(personVariable);
  }
  
  @Override
  public void delete(PersonVariable personVariable) {
    super.delete(personVariable);
  }

}
