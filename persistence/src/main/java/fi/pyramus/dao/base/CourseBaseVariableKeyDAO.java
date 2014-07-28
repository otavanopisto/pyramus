package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey_;
import fi.pyramus.domainmodel.base.VariableType;

@Stateless
public class CourseBaseVariableKeyDAO extends PyramusEntityDAO<CourseBaseVariableKey> {

  public CourseBaseVariableKey create(String variableKey, String variableName, VariableType variableType, Boolean userEditable) {
    CourseBaseVariableKey courseBaseVariableKey = new CourseBaseVariableKey();
    courseBaseVariableKey.setUserEditable(userEditable);
    courseBaseVariableKey.setVariableKey(variableKey);
    courseBaseVariableKey.setVariableName(variableName);
    courseBaseVariableKey.setVariableType(variableType);
    return persist(courseBaseVariableKey);
  }

  public CourseBaseVariableKey findByVariableKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseBaseVariableKey> criteria = criteriaBuilder.createQuery(CourseBaseVariableKey.class);
    Root<CourseBaseVariableKey> root = criteria.from(CourseBaseVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseBaseVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public CourseBaseVariableKey updateVariableKey(CourseBaseVariableKey courseBaseVariableKey, String variableKey) {
    courseBaseVariableKey.setVariableKey(variableKey);
    return persist(courseBaseVariableKey);
  }

  public CourseBaseVariableKey updateVariableName(CourseBaseVariableKey courseBaseVariableKey, String variableName) {
    courseBaseVariableKey.setVariableName(variableName);
    return persist(courseBaseVariableKey);
  }

  public CourseBaseVariableKey updateVariableType(CourseBaseVariableKey courseBaseVariableKey, VariableType variableType) {
    courseBaseVariableKey.setVariableType(variableType);
    return persist(courseBaseVariableKey);
  }

  public CourseBaseVariableKey updateUserEditable(CourseBaseVariableKey courseBaseVariableKey, Boolean userEditable) {
    courseBaseVariableKey.setUserEditable(userEditable);
    return persist(courseBaseVariableKey);
  }

}
