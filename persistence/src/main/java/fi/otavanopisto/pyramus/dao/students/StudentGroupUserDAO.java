package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser_;
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
  
  public StudentGroupUser create(StudentGroup studentGroup, StaffMember staffMember, User updatingUser) {
    EntityManager entityManager = getEntityManager();
    StudentGroupUser sgu = new StudentGroupUser();
    sgu.setStaffMember(staffMember);
    
    entityManager.persist(sgu);

    studentGroup.addUser(sgu);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    
    staffMemberCreatedEvent.fire(new StudentGroupStaffMemberCreatedEvent(sgu.getId(), sgu.getStudentGroup().getId(), sgu.getStaffMember().getId()));
    
    return sgu;
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
  
}
