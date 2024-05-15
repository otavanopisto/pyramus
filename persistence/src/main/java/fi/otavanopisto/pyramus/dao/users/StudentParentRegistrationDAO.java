package fi.otavanopisto.pyramus.dao.users;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentRegistration;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentRegistration_;

@Stateless
public class StudentParentRegistrationDAO extends PyramusEntityDAO<StudentParentRegistration> {

  public StudentParentRegistration create(String firstName, String lastName, String email, Student student, String hash) {
    StudentParentRegistration studentParentRegistration = new StudentParentRegistration();
    studentParentRegistration.setFirstName(firstName);
    studentParentRegistration.setLastName(lastName);
    studentParentRegistration.setEmail(email);
    studentParentRegistration.setStudent(student);
    studentParentRegistration.setHash(hash);
    studentParentRegistration.setCreated(new Date());

    return persist(studentParentRegistration);
  }
  
  public StudentParentRegistration findByHash(String hash) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParentRegistration> criteria = criteriaBuilder.createQuery(StudentParentRegistration.class);
    Root<StudentParentRegistration> root = criteria.from(StudentParentRegistration.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(StudentParentRegistration_.hash), hash));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<StudentParentRegistration> listBy(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParentRegistration> criteria = criteriaBuilder.createQuery(StudentParentRegistration.class);
    Root<StudentParentRegistration> root = criteria.from(StudentParentRegistration.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(StudentParentRegistration_.student), student));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
