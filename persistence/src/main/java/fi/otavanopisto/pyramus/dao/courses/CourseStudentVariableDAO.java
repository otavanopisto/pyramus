package fi.otavanopisto.pyramus.dao.courses;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudentVariable;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudentVariableKey;
import fi.pyramus.domainmodel.courses.CourseStudentVariable_;

@Stateless
public class CourseStudentVariableDAO extends PyramusEntityDAO<CourseStudentVariable> {

  public CourseStudentVariable create(CourseStudent courseStudent, CourseStudentVariableKey key, String value) {
    EntityManager entityManager = getEntityManager(); 

    CourseStudentVariable courseStudentVariable = new CourseStudentVariable();
    courseStudentVariable.setCourseStudent(courseStudent);
    courseStudentVariable.setKey(key);
    courseStudentVariable.setValue(value);
    entityManager.persist(courseStudentVariable);

    return courseStudentVariable;
  }

  public CourseStudentVariable update(CourseStudentVariable courseStudentVariable, String value) {
    EntityManager entityManager = getEntityManager(); 
    courseStudentVariable.setValue(value);
    entityManager.persist(courseStudentVariable);
    return courseStudentVariable;
  }

  public void setCourseStudentVariable(CourseStudent courseStudent, String key, String value) {
    CourseStudentVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getCourseStudentVariableKeyDAO();
    CourseStudentVariableKey variableKey = variableKeyDAO.findByKey(key);

    if (variableKey != null) {
      CourseStudentVariable variable = findByCourseStudentAndKey(courseStudent, variableKey);
      if (StringUtils.isBlank(value)) {
        if (variable != null) {
          delete(variable);
        }
      } else {
        if (variable == null) {
          variable = create(courseStudent, variableKey, value);
        } else {
          update(variable, value);
        }
      }
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }
  
  public CourseStudentVariable findByCourseStudentAndKey(CourseStudent courseStudent, CourseStudentVariableKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudentVariable> criteria = criteriaBuilder.createQuery(CourseStudentVariable.class);
    Root<CourseStudentVariable> root = criteria.from(CourseStudentVariable.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseStudentVariable_.courseStudent), courseStudent),
            criteriaBuilder.equal(root.get(CourseStudentVariable_.key), key)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public String findByCourseStudentAndKey(CourseStudent courseStudent, String key) {
    CourseStudentVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getCourseStudentVariableKeyDAO();
    CourseStudentVariableKey variableKey = variableKeyDAO.findByKey(key);
    
    if (variableKey != null) {
      CourseStudentVariable variable = findByCourseStudentAndKey(courseStudent, variableKey);
      return variable == null ? null : variable.getValue();
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  @Override
  public void delete(CourseStudentVariable courseStudentVariable) {
    super.delete(courseStudentVariable);
  }
}
