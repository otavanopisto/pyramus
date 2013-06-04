package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.courses.CourseStudentVariableKey;
import fi.pyramus.domainmodel.courses.CourseStudentVariableKey_;

@Stateless
public class CourseStudentVariableKeyDAO extends PyramusEntityDAO<CourseStudentVariableKey> {

  public CourseStudentVariableKey create(boolean userEditable, String variableKey, String variableName, VariableType variableType) {
    EntityManager entityManager = getEntityManager();
      
    CourseStudentVariableKey courseStudentVariableKey = new CourseStudentVariableKey();
    courseStudentVariableKey.setUserEditable(userEditable);
    courseStudentVariableKey.setVariableKey(variableKey);
    courseStudentVariableKey.setVariableName(variableName);
    courseStudentVariableKey.setVariableType(variableType);
    entityManager.persist(courseStudentVariableKey);
    return courseStudentVariableKey;
  }
  
  public CourseStudentVariableKey findByKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudentVariableKey> criteria = criteriaBuilder.createQuery(CourseStudentVariableKey.class);
    Root<CourseStudentVariableKey> root = criteria.from(CourseStudentVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseStudentVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Returns a list of user editable student variable keys from the database, sorted by their user interface name.
   * 
   * @return A list of user editable student variable keys
   */
  public List<CourseStudentVariableKey> listUserEditableCourseStudentVariableKeys() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudentVariableKey> criteria = criteriaBuilder.createQuery(CourseStudentVariableKey.class);
    Root<CourseStudentVariableKey> root = criteria.from(CourseStudentVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseStudentVariableKey_.userEditable), Boolean.TRUE)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
