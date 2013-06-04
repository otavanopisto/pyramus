package fi.pyramus.dao.grading;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.grading.Credit;
import fi.pyramus.domainmodel.grading.CreditVariable;
import fi.pyramus.domainmodel.grading.CreditVariable_;
import fi.pyramus.domainmodel.grading.CreditVariableKey;

@Stateless
public class CreditVariableDAO extends PyramusEntityDAO<CreditVariable> {

  public CreditVariable create(Credit credit, CreditVariableKey key, String value) {
    EntityManager entityManager = getEntityManager(); 

    CreditVariable creditVariable = new CreditVariable();
    creditVariable.setCredit(credit);
    creditVariable.setKey(key);
    creditVariable.setValue(value);
    entityManager.persist(creditVariable);

//    credit.getVariables().add(creditVariable);
//    entityManager.persist(credit);

    return creditVariable;
  }

  public CreditVariable update(CreditVariable creditVariable, String value) {
    EntityManager entityManager = getEntityManager(); 
    creditVariable.setValue(value);
    entityManager.persist(creditVariable);
    return creditVariable;
  }

  public void setCreditVariable(Credit credit, String key, String value) {
    CreditVariableKeyDAO creditVariableKeyDAO = DAOFactory.getInstance().getCreditVariableKeyDAO();
    CreditVariableKey creditVariableKey = creditVariableKeyDAO.findByKey(key);

    if (creditVariableKey != null) {
      CreditVariable creditVariable = findByCreditAndKey(credit, creditVariableKey);
      if (StringUtils.isBlank(value)) {
        if (creditVariable != null) {
          delete(creditVariable);
        }
      } else {
        if (creditVariable == null) {
          creditVariable = create(credit, creditVariableKey, value);
        } else {
          update(creditVariable, value);
        }
      }
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }
  
  public CreditVariable findByCreditAndKey(Credit credit, CreditVariableKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditVariable> criteria = criteriaBuilder.createQuery(CreditVariable.class);
    Root<CreditVariable> root = criteria.from(CreditVariable.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditVariable_.credit), credit),
            criteriaBuilder.equal(root.get(CreditVariable_.key), key)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public String findByCreditAndKey(Credit credit, String key) {
    CreditVariableKeyDAO creditVariableKeyDAO = DAOFactory.getInstance().getCreditVariableKeyDAO();
    CreditVariableKey creditVariableKey = creditVariableKeyDAO.findByKey(key);
    
    if (creditVariableKey != null) {
      CreditVariable creditVariable = findByCreditAndKey(credit, creditVariableKey);
      return creditVariable == null ? null : creditVariable.getValue();
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  @Override
  public void delete(CreditVariable studentVariable) {
    super.delete(studentVariable);
  }
}
