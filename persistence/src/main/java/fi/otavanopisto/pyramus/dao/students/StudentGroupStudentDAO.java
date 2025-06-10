package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.Predicates;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.StudentGroupStudentCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentRemovedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentUpdatedEvent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent_;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup_;

@Stateless
public class StudentGroupStudentDAO extends PyramusEntityDAO<StudentGroupStudent> {

  @Inject
  private Event<StudentGroupStudentCreatedEvent> studentCreatedEvent;
  
  @Inject
  private Event<StudentGroupStudentUpdatedEvent> studentUpdatedEvent;
  
  @Inject
  private Event<StudentGroupStudentRemovedEvent> studentRemovedEvent;
  
  public StudentGroupStudent create(StudentGroup studentGroup, Student student, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    StudentGroupStudent sgs = new StudentGroupStudent();
    sgs.setStudent(student);
    entityManager.persist(sgs);
    
    studentGroup.addStudent(sgs);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    
    studentCreatedEvent.fire(new StudentGroupStudentCreatedEvent(sgs.getId(), sgs.getStudentGroup().getId(), sgs.getStudent().getId()));
   
    return sgs;
  }

  public StudentGroupStudent findByStudentGroupAndStudent(StudentGroup studentGroup, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroupStudent> criteria = criteriaBuilder.createQuery(StudentGroupStudent.class);
    Root<StudentGroupStudent> root = criteria.from(StudentGroupStudent.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(StudentGroupStudent_.student), student),
        criteriaBuilder.equal(root.get(StudentGroupStudent_.studentGroup), studentGroup)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<StudentGroupStudent> listByStudent(Student student, Boolean onlyGuidanceGroups) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroupStudent> criteria = criteriaBuilder.createQuery(StudentGroupStudent.class);
    Root<StudentGroupStudent> root = criteria.from(StudentGroupStudent.class);
    Join<StudentGroupStudent, StudentGroup> studentGroupJoin = root.join(StudentGroupStudent_.studentGroup);
    

    Predicates predicates = Predicates.newInstance()
        .add(criteriaBuilder.equal(root.get(StudentGroupStudent_.student), student))
        .add(criteriaBuilder.equal(studentGroupJoin.get(StudentGroup_.archived), Boolean.FALSE))
        .add(criteriaBuilder.equal(studentGroupJoin.get(StudentGroup_.guidanceGroup), Boolean.TRUE), Boolean.TRUE.equals(onlyGuidanceGroups));
    
    criteria.select(root);
    criteria.where(predicates.array());
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public void update(StudentGroupStudent studentGroupStudent, Student student, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    StudentGroup studentGroup = studentGroupStudent.getStudentGroup();
    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));
    entityManager.persist(studentGroup);
    
    studentGroupStudent.setStudent(student);
    entityManager.persist(studentGroupStudent);

    studentUpdatedEvent.fire(new StudentGroupStudentUpdatedEvent(studentGroupStudent.getId(), studentGroup.getId(), student.getId()));
  }

  public void remove(StudentGroup studentGroup, StudentGroupStudent studentGroupStudent, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    studentGroup.removeStudent(studentGroupStudent);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    entityManager.remove(studentGroupStudent);

    studentRemovedEvent.fire(new StudentGroupStudentRemovedEvent(studentGroupStudent.getId(), studentGroup.getId(), studentGroupStudent.getStudent().getId()));
  }

  @Override
  public void delete(StudentGroupStudent studentGroupStudent) {
    super.delete(studentGroupStudent);
    studentRemovedEvent.fire(new StudentGroupStudentRemovedEvent(
        studentGroupStudent.getId(),
        studentGroupStudent.getStudentGroup().getId(),
        studentGroupStudent.getStudent().getId()));
  }
  
}
