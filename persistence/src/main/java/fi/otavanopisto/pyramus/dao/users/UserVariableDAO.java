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
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey_;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable_;

@Stateless
public class UserVariableDAO extends PyramusEntityDAO<UserVariable> {

  public UserVariable create(User user, UserVariableKey key, String value) {
    UserVariable userVariable = new UserVariable();
    userVariable.setUser(user);
    userVariable.setKey(key);
    userVariable.setValue(value);
    
    return persist(userVariable);
  }

  public void createDefaultValueVariables(User user) {
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();

    List<UserVariableKey> defaultValueVariables = variableKeyDAO.listByExistingDefaultValue();
    
    for (UserVariableKey defaultValueVariable : defaultValueVariables) {
      if (StringUtils.isNotBlank(defaultValueVariable.getDefaultValueOnCreation())) {
        create(user, defaultValueVariable, defaultValueVariable.getDefaultValueOnCreation());
      }
    }
  }

  public UserVariable findByUserAndVariableKey(User user, UserVariableKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserVariable> criteria = criteriaBuilder.createQuery(UserVariable.class);
    Root<UserVariable> root = criteria.from(UserVariable.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(UserVariable_.user), user),
            criteriaBuilder.equal(root.get(UserVariable_.key), key)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public String findByUserAndKey(User user, String key) {
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    
    UserVariableKey userVariableKey = variableKeyDAO.findByVariableKey(key);
    if (userVariableKey != null) {
      UserVariable userVariable = findByUserAndVariableKey(user, userVariableKey); 
      return userVariable == null ? null : userVariable.getValue();
    }
    else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  public List<UserVariable> listByUser(User user) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserVariable> criteria = criteriaBuilder.createQuery(UserVariable.class);
    Root<UserVariable> root = criteria.from(UserVariable.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(UserVariable_.user), user)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<UserVariable> listByUserAndUserEditable(User user, Boolean userEditable) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserVariable> criteria = criteriaBuilder.createQuery(UserVariable.class);
    Root<UserVariable> root = criteria.from(UserVariable.class);
    Join<UserVariable, UserVariableKey> key = root.join(UserVariable_.key);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(UserVariable_.user), user),
            criteriaBuilder.equal(key.get(UserVariableKey_.userEditable), userEditable)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public void setUserVariable(User user, String key, String value) {
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    
    UserVariableKey userVariableKey = variableKeyDAO.findByVariableKey(key);
    if (userVariableKey != null) {
      UserVariable userVariable = findByUserAndVariableKey(user, userVariableKey);
      if (StringUtils.isBlank(value)) {
        if (userVariable != null) {
          delete(userVariable);
        }
      }
      else {
        if (userVariable == null) {
          userVariable = create(user, userVariableKey, value);
        }
        else {
          updateValue(userVariable, value);
        }
      }
    }
    else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }
  
  public UserVariable updateValue(UserVariable userVariable, String value) {
    userVariable.setValue(value);
    return persist(userVariable);
  }
  
  @Override
  public void delete(UserVariable userVariable) {
    super.delete(userVariable);
  }

}
