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
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentInvitation;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentInvitation_;

@Stateless
public class StudentParentInvitationDAO extends PyramusEntityDAO<StudentParentInvitation> {

  public StudentParentInvitation create(String firstName, String lastName, String email, Student student, String hash) {
    StudentParentInvitation studentParentInvitation = new StudentParentInvitation();
    studentParentInvitation.setFirstName(firstName);
    studentParentInvitation.setLastName(lastName);
    studentParentInvitation.setEmail(email);
    studentParentInvitation.setStudent(student);
    studentParentInvitation.setHash(hash);
    studentParentInvitation.setCreated(new Date());

    return persist(studentParentInvitation);
  }
  
  public StudentParentInvitation findByHash(String hash) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParentInvitation> criteria = criteriaBuilder.createQuery(StudentParentInvitation.class);
    Root<StudentParentInvitation> root = criteria.from(StudentParentInvitation.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(StudentParentInvitation_.hash), hash));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<StudentParentInvitation> listBy(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParentInvitation> criteria = criteriaBuilder.createQuery(StudentParentInvitation.class);
    Root<StudentParentInvitation> root = criteria.from(StudentParentInvitation.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(StudentParentInvitation_.student), student));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudentParentInvitation updateHashAndDate(StudentParentInvitation invitation, String hash, Date date) {
    invitation.setHash(hash);
    invitation.setCreated(date);
    return persist(invitation);
  }
  
}
