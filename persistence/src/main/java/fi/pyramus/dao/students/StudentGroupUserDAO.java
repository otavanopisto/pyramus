package fi.pyramus.dao.students;

import java.util.Date;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupUser;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.events.StudentGroupStaffMemberCreatedEvent;
import fi.pyramus.events.StudentGroupStaffMemberRemovedEvent;

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

  public void remove(StudentGroup studentGroup, StudentGroupUser user, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    studentGroup.removeUser(user);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    entityManager.remove(user);

    staffMemberRemovedEvent.fire(new StudentGroupStaffMemberRemovedEvent(user.getId(), studentGroup.getId(), user.getStaffMember().getId()));
  }
  
}
