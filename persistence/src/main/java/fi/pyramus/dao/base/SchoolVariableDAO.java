package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.domainmodel.base.SchoolVariable_;

@Stateless
public class SchoolVariableDAO extends PyramusEntityDAO<SchoolVariable> {

  private SchoolVariable create(School school, SchoolVariableKey key, String value) {
    EntityManager entityManager = getEntityManager();

    SchoolVariable schoolVariable = new SchoolVariable();
    schoolVariable.setSchool(school);
    schoolVariable.setKey(key);
    schoolVariable.setValue(value);
    entityManager.persist(schoolVariable);

    school.getVariables().add(schoolVariable);
    entityManager.persist(school);

    return schoolVariable;
  }

  private SchoolVariable findBySchoolAndVariableKey(School school, SchoolVariableKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SchoolVariable> criteria = criteriaBuilder.createQuery(SchoolVariable.class);
    Root<SchoolVariable> root = criteria.from(SchoolVariable.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(SchoolVariable_.school), school),
            criteriaBuilder.equal(root.get(SchoolVariable_.key), key)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public String findValueBySchoolAndKey(School school, String key) {
    SchoolVariableKeyDAO schoolVariableKeyDAO = DAOFactory.getInstance().getSchoolVariableKeyDAO();
    SchoolVariableKey schoolVariableKey = schoolVariableKeyDAO.findVariableKey(key);
    if (schoolVariableKey != null) {
      SchoolVariable schoolVariable = findBySchoolAndVariableKey(school, schoolVariableKey);
      return schoolVariable == null ? null : schoolVariable.getValue();
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  public SchoolVariable update(SchoolVariable schoolVariable, String value) {
    EntityManager entityManager = getEntityManager();
    schoolVariable.setValue(value);
    entityManager.persist(schoolVariable);
    return schoolVariable;
  }

  public void setVariable(School school, String key, String value) {
    SchoolVariableKeyDAO schoolVariableKeyDAO = DAOFactory.getInstance().getSchoolVariableKeyDAO();
    
    SchoolVariableKey schoolVariableKey = schoolVariableKeyDAO.findVariableKey(key);
    if (schoolVariableKey != null) {
      SchoolVariable schoolVariable = findBySchoolAndVariableKey(school, schoolVariableKey);
      if (StringUtils.isBlank(value)) {
        if (schoolVariable != null) {
          delete(schoolVariable);
        }
      } else {
        if (schoolVariable == null) {
          schoolVariable = create(school, schoolVariableKey, value);
        } else {
          update(schoolVariable, value);
        }
      }
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

}
