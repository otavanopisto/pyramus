package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.events.StudentSubjectGradeEvent;
import fi.otavanopisto.pyramus.events.types.Created;
import fi.otavanopisto.pyramus.events.types.Removed;
import fi.otavanopisto.pyramus.events.types.Updated;

@Stateless
public class StudentSubjectGradeDAO extends PyramusEntityDAO<StudentSubjectGrade> {

  @Inject
  @Created
  private Event<StudentSubjectGradeEvent> studentSubjectGradeCreatedEvent;

  @Inject
  @Updated
  private Event<StudentSubjectGradeEvent> studentSubjectGradeUpdatedEvent;
  
  @Inject
  @Removed
  private Event<StudentSubjectGradeEvent> studentSubjectGradeRemovedEvent;
  
  public StudentSubjectGrade create(Student student, Subject subject, StaffMember issuer, Grade grade, Date gradeDate, StaffMember gradeApprover, String explanation) {
    StudentSubjectGrade studentSubjectGrade = new StudentSubjectGrade();
    studentSubjectGrade.setStudent(student);
    studentSubjectGrade.setSubject(subject);
    studentSubjectGrade.setGrade(grade);
    studentSubjectGrade.setIssuer(issuer);
    studentSubjectGrade.setExplanation(explanation);
    studentSubjectGrade.setGradeDate(gradeDate);
    studentSubjectGrade.setGradeApprover(gradeApprover);
    
    return fireCreatedEvent(persist(studentSubjectGrade));
  }
  
  public List<StudentSubjectGrade> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentSubjectGrade> criteria = criteriaBuilder.createQuery(StudentSubjectGrade.class);
    Root<StudentSubjectGrade> root = criteria.from(StudentSubjectGrade.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentSubjectGrade_.student), student)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudentSubjectGrade findBy(Student student, Subject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentSubjectGrade> criteria = criteriaBuilder.createQuery(StudentSubjectGrade.class);
    Root<StudentSubjectGrade> root = criteria.from(StudentSubjectGrade.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentSubjectGrade_.student), student),
        criteriaBuilder.equal(root.get(StudentSubjectGrade_.subject), subject)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public StudentSubjectGrade updateGrade(StudentSubjectGrade studentSubjectGrade, StaffMember issuer, Grade grade, Date gradeDate, StaffMember gradeApprover, String explanation) {
    studentSubjectGrade.setGrade(grade);
    studentSubjectGrade.setIssuer(issuer);
    studentSubjectGrade.setExplanation(explanation);
    studentSubjectGrade.setGradeDate(gradeDate);
    studentSubjectGrade.setGradeApprover(gradeApprover);
    return fireUpdatedEvent(persist(studentSubjectGrade));
  }

  @Override
  public void delete(StudentSubjectGrade e) {
    super.delete(e);
    
    fireRemovedEvent(e);
  }

  private StudentSubjectGrade fireCreatedEvent(StudentSubjectGrade studentSubjectGrade) {
    Long studentId = studentSubjectGrade.getStudent() != null ? studentSubjectGrade.getStudent().getId() : null;
    studentSubjectGradeCreatedEvent.fire(new StudentSubjectGradeEvent(studentSubjectGrade.getId(), studentId));
    return studentSubjectGrade;
  }

  private StudentSubjectGrade fireUpdatedEvent(StudentSubjectGrade studentSubjectGrade) {
    Long studentId = studentSubjectGrade.getStudent() != null ? studentSubjectGrade.getStudent().getId() : null;
    studentSubjectGradeUpdatedEvent.fire(new StudentSubjectGradeEvent(studentSubjectGrade.getId(), studentId));
    return studentSubjectGrade;
  }

  private StudentSubjectGrade fireRemovedEvent(StudentSubjectGrade studentSubjectGrade) {
    Long studentId = studentSubjectGrade.getStudent() != null ? studentSubjectGrade.getStudent().getId() : null;
    studentSubjectGradeRemovedEvent.fire(new StudentSubjectGradeEvent(studentSubjectGrade.getId(), studentId));
    return studentSubjectGrade;
  }
  
}
