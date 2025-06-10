package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCardActivity;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCardType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard_;

@Stateless
public class StudentCardDAO extends PyramusEntityDAO<StudentCard> {
 
  public StudentCard create(Student student, StudentCardActivity activity, Date expiryDate, StudentCardType type) {
    EntityManager entityManager = getEntityManager();
    
    StudentCard studentCard = new StudentCard();
    studentCard.setStudent(student);
    studentCard.setActivity(activity);
    studentCard.setExpiryDate(expiryDate);
    studentCard.setType(type);
    
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard updateActive(StudentCard studentCard, StudentCardActivity activity, Date cancellationDate){
    EntityManager entityManager = getEntityManager();
    
    studentCard.setActivity(activity);
    studentCard.setCancellationDate(cancellationDate);
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard updateCancellationDate(StudentCard studentCard, Date cancellationDate) {
    EntityManager entityManager = getEntityManager();
    
    studentCard.setCancellationDate(cancellationDate);
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard updateExpiryDate(StudentCard studentCard, Date expiryDate) {
    EntityManager entityManager = getEntityManager();
    
    studentCard.setExpiryDate(expiryDate);
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard update(StudentCard studentCard, StudentCardActivity activity, Date expiryDate, StudentCardType type, Date cancellationDate) {
    EntityManager entityManager = getEntityManager();
    
    studentCard.setExpiryDate(expiryDate);
    studentCard.setActivity(activity);
    studentCard.setType(type);
    studentCard.setCancellationDate(cancellationDate);
    entityManager.persist(studentCard);
    return studentCard;
  }
  
  public StudentCard findByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentCard> criteria = criteriaBuilder.createQuery(StudentCard.class);
    Root<StudentCard> root = criteria.from(StudentCard.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentCard_.student), student)
      );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<StudentCard> listStudentCardsByExpiryDateAndActive(Date twoWeeksBefore) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentCard> criteria = criteriaBuilder.createQuery(StudentCard.class);
    Root<StudentCard> root = criteria.from(StudentCard.class);
    
    // Select student cards where expiration date is within the past two weeks or in the future and student card activity is not 'inactive'
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.greaterThanOrEqualTo(root.get(StudentCard_.expiryDate), twoWeeksBefore),
            criteriaBuilder.notEqual(root.get(StudentCard_.activity), StudentCardActivity.INACTIVE),
            criteriaBuilder.isNotNull(root.get(StudentCard_.activity)),
            criteriaBuilder.isNotNull(root.get(StudentCard_.type))
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
}
