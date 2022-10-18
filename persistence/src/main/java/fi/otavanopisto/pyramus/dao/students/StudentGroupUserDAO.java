package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import fi.otavanopisto.pyramus.dao.Predicates;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent_;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser_;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberRemovedEvent;

@Stateless
public class StudentGroupUserDAO extends PyramusEntityDAO<StudentGroupUser> {

  @Inject
  private Event<StudentGroupStaffMemberCreatedEvent> staffMemberCreatedEvent;
  
//  @Inject
//  private Event<StudentGroupStaffMemberUpdatedEvent> studentUpdatedEvent;
  
  @Inject
  private Event<StudentGroupStaffMemberRemovedEvent> staffMemberRemovedEvent;
  
  public StudentGroupUser create(StudentGroup studentGroup, StaffMember staffMember, Boolean messageRecipient, User updatingUser) {
    EntityManager entityManager = getEntityManager();
    StudentGroupUser sgu = new StudentGroupUser();
    sgu.setStaffMember(staffMember);
    sgu.setMessageRecipient(messageRecipient);
    
    entityManager.persist(sgu);

    studentGroup.addUser(sgu);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    
    staffMemberCreatedEvent.fire(new StudentGroupStaffMemberCreatedEvent(sgu.getId(), sgu.getStudentGroup().getId(), sgu.getStaffMember().getId()));
    
    return sgu;
  }

  public StudentGroupUser update(StudentGroupUser studentGroupUser, Boolean messageRecipient) {
    studentGroupUser.setMessageRecipient(messageRecipient);
    return persist(studentGroupUser);
  }
  
  public void remove(StudentGroup studentGroup, StudentGroupUser studentGroupUser, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    studentGroup.removeUser(studentGroupUser);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    entityManager.remove(studentGroupUser);

    staffMemberRemovedEvent.fire(new StudentGroupStaffMemberRemovedEvent(studentGroupUser.getId(), studentGroup.getId(), studentGroupUser.getStaffMember().getId()));
  }

  @Override
  public void delete(StudentGroupUser studentGroupUser) {
    super.delete(studentGroupUser);
    staffMemberRemovedEvent.fire(new StudentGroupStaffMemberRemovedEvent(
        studentGroupUser.getId(),
        studentGroupUser.getStudentGroup().getId(),
        studentGroupUser.getStaffMember().getId()));
  }

  public StudentGroupUser findByStudentGroupAndStaffMember(StudentGroup studentGroup, StaffMember staffMember) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroupUser> criteria = criteriaBuilder.createQuery(StudentGroupUser.class);
    Root<StudentGroupUser> root = criteria.from(StudentGroupUser.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(StudentGroupUser_.staffMember), staffMember),
        criteriaBuilder.equal(root.get(StudentGroupUser_.studentGroup), studentGroup)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Lists StudentGroupUsers that are in the same groups as given student
   * 
   * @param student student
   * @param onlyGuidanceGroups
   * @param onlyMessageRecipients 
   * @return StudentGroupUsers that are in the same groups as given student
   */
  public List<StudentGroupUser> listByStudent(Student student, Boolean onlyGuidanceGroups, Boolean onlyMessageRecipients) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroupUser> criteria = criteriaBuilder.createQuery(StudentGroupUser.class);
    Root<StudentGroupUser> root = criteria.from(StudentGroupUser.class);
    Join<StudentGroupUser, StudentGroup> studentGroupJoin = root.join(StudentGroupUser_.studentGroup);
    
    Subquery<StudentGroup> subquery = criteria.subquery(StudentGroup.class);
    Root<StudentGroupStudent> subqueryFrom = subquery.from(StudentGroupStudent.class);
    subquery.select(subqueryFrom.get(StudentGroupStudent_.studentGroup));
    subquery.where(criteriaBuilder.equal(subqueryFrom.get(StudentGroupStudent_.student), student));

    Predicates predicates = Predicates.newInstance()
        .add(root.get(StudentGroupUser_.studentGroup).in(subquery))
        .add(criteriaBuilder.equal(studentGroupJoin.get(StudentGroup_.guidanceGroup), Boolean.TRUE), Boolean.TRUE.equals(onlyGuidanceGroups))
        .add(criteriaBuilder.equal(root.get(StudentGroupUser_.messageRecipient), Boolean.TRUE), Boolean.TRUE.equals(onlyMessageRecipients));
    
    criteria.select(root);
    criteria.where(predicates.array());
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
