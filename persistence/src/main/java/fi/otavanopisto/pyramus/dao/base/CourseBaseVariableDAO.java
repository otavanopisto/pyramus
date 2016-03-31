package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariable;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.base.CourseBaseVariable_;

@Stateless
public class CourseBaseVariableDAO extends PyramusEntityDAO<CourseBaseVariable> {

  public CourseBaseVariable create(Course course, CourseBaseVariableKey key, String value) {
    EntityManager entityManager = getEntityManager();

    CourseBaseVariable courseBaseVariable = new CourseBaseVariable();
    courseBaseVariable.setCourseBase(course);
    courseBaseVariable.setKey(key);
    courseBaseVariable.setValue(value);
    entityManager.persist(courseBaseVariable);

    course.getVariables().add(courseBaseVariable);
    entityManager.persist(course);

    return courseBaseVariable;
  }

  public CourseBaseVariable findByCourseAndVariableKey(Course course, CourseBaseVariableKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseBaseVariable> criteria = criteriaBuilder.createQuery(CourseBaseVariable.class);
    Root<CourseBaseVariable> root = criteria.from(CourseBaseVariable.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseBaseVariable_.courseBase), course),
            criteriaBuilder.equal(root.get(CourseBaseVariable_.key), key)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public String findByCourseAndVariableKey(Course course, String key) {
    CourseBaseVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getCourseBaseVariableKeyDAO();
    CourseBaseVariableKey courseBaseVariableKey = variableKeyDAO.findByVariableKey(key);
    if (courseBaseVariableKey != null) {
      CourseBaseVariable courseBaseVariable = findByCourseAndVariableKey(course, courseBaseVariableKey);
      return courseBaseVariable == null ? null : courseBaseVariable.getValue();
    }
    else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  public CourseBaseVariable update(CourseBaseVariable courseBaseVariable, String value) {
    EntityManager entityManager = getEntityManager();
    courseBaseVariable.setValue(value);
    entityManager.persist(courseBaseVariable);
    return courseBaseVariable;
  }

  public void setCourseVariable(Course course, String key, String value) {
    CourseBaseVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getCourseBaseVariableKeyDAO();
    CourseBaseVariableKey courseBaseVariableKey = variableKeyDAO.findByVariableKey(key);
    if (courseBaseVariableKey != null) {
      CourseBaseVariable courseBaseVariable = findByCourseAndVariableKey(course, courseBaseVariableKey);
      if (StringUtils.isBlank(value)) {
        delete(courseBaseVariable);
      }
      else {
        if (courseBaseVariable == null) {
          courseBaseVariable = create(course, courseBaseVariableKey, value);
        }
        else {
          update(courseBaseVariable, value);
        }
      }
    }
    else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  public List<CourseBaseVariable> listByCourseBase(CourseBase courseBase) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseBaseVariable> criteria = criteriaBuilder.createQuery(CourseBaseVariable.class);
    Root<CourseBaseVariable> root = criteria.from(CourseBaseVariable.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(CourseBaseVariable_.courseBase), courseBase)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
