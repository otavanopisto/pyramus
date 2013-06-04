package fi.pyramus.dao.students;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentVariable;
import fi.pyramus.domainmodel.students.StudentVariableKey;
import fi.pyramus.domainmodel.students.StudentVariable_;

@Stateless
public class StudentVariableDAO extends PyramusEntityDAO<StudentVariable> {

  public StudentVariable create(Student student, StudentVariableKey key, String value) {
    EntityManager entityManager = getEntityManager(); 

    StudentVariable studentVariable = new StudentVariable();
    studentVariable.setStudent(student);
    studentVariable.setKey(key);
    studentVariable.setValue(value);
    entityManager.persist(studentVariable);

    student.getVariables().add(studentVariable);
    entityManager.persist(student);

    return studentVariable;
  }

  public StudentVariable update(StudentVariable studentVariable, String value) {
    EntityManager entityManager = getEntityManager(); 
    studentVariable.setValue(value);
    entityManager.persist(studentVariable);
    return studentVariable;
  }

  public void setStudentVariable(Student student, String key, String value) {
    StudentVariableKeyDAO studentVariableKeyDAO = DAOFactory.getInstance().getStudentVariableKeyDAO();
    StudentVariableKey studentVariableKey = studentVariableKeyDAO.findByKey(key);

    if (studentVariableKey != null) {
      StudentVariable studentVariable = findByStudentAndKey(student, studentVariableKey);
      if (StringUtils.isBlank(value)) {
        if (studentVariable != null) {
          delete(studentVariable);
        }
      } else {
        if (studentVariable == null) {
          studentVariable = create(student, studentVariableKey, value);
        } else {
          update(studentVariable, value);
        }
      }
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }
  
  public StudentVariable findByStudentAndKey(Student student, StudentVariableKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentVariable> criteria = criteriaBuilder.createQuery(StudentVariable.class);
    Root<StudentVariable> root = criteria.from(StudentVariable.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentVariable_.student), student),
            criteriaBuilder.equal(root.get(StudentVariable_.key), key)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public String findByStudentAndKey(Student student, String key) {
    StudentVariableKeyDAO studentVariableKeyDAO = DAOFactory.getInstance().getStudentVariableKeyDAO();
    StudentVariableKey studentVariableKey = studentVariableKeyDAO.findByKey(key);
    
    if (studentVariableKey != null) {
      StudentVariable studentVariable = findByStudentAndKey(student, studentVariableKey);
      return studentVariable == null ? null : studentVariable.getValue();
    } else {
      throw new PersistenceException("Unknown VariableKey");
    }
  }

  @Override
  public void delete(StudentVariable studentVariable) {
    super.delete(studentVariable);
  }
}
