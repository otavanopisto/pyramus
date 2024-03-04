package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCardType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard_;

@Stateless
public class StudentCardDAO extends PyramusEntityDAO<StudentCard> {
 
  public StudentCard create(Student student, Boolean active, Date expiryDate, StudentCardType type) {
    EntityManager entityManager = getEntityManager();
    
    StudentCard studentCard = new StudentCard();
    studentCard.setStudent(student);
    studentCard.setActive(active);
    studentCard.setExpiryDate(expiryDate);
    studentCard.setType(type);
    
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard updateActive(StudentCard studentCard, Boolean active) {
    EntityManager entityManager = getEntityManager();
    
    studentCard.setActive(active);
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard updateExpiryDate(StudentCard studentCard, Date expiryDate) {
    EntityManager entityManager = getEntityManager();
    
    studentCard.setExpiryDate(expiryDate);
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard update(StudentCard studentCard, Boolean active, Date expiryDate, StudentCardType type) {
    EntityManager entityManager = getEntityManager();
    
    studentCard.setExpiryDate(expiryDate);
    studentCard.setActive(active);
    studentCard.setType(type);
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard findByStudent(Long studentId) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentCard> criteria = criteriaBuilder.createQuery(StudentCard.class);
    Root<StudentCard> root = criteria.from(StudentCard.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentCard_.student), studentId)
      );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
}
