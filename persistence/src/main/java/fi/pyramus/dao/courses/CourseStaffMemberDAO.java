package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseStaffMember;
import fi.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.pyramus.domainmodel.courses.CourseStaffMember_;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.events.CourseStaffMemberCreatedEvent;
import fi.pyramus.events.CourseStaffMemberDeletedEvent;
import fi.pyramus.events.CourseStaffMemberUpdatedEvent;

@Stateless
public class CourseStaffMemberDAO extends PyramusEntityDAO<CourseStaffMember> {

  @Inject
  private Event<CourseStaffMemberCreatedEvent> courseStaffMemberCreatedEvent;

  @Inject
  private Event<CourseStaffMemberUpdatedEvent> courseStaffMemberUpdatedEvent;
  
  @Inject
  private Event<CourseStaffMemberDeletedEvent> courseStaffMemberDeletedEvent;

  public CourseStaffMember create(Course course, StaffMember staffMember, CourseStaffMemberRole role) {
    CourseStaffMember courseStaffMember = new CourseStaffMember();
    courseStaffMember.setCourse(course);
    courseStaffMember.setStaffMember(staffMember);
    courseStaffMember.setRole(role);
    persist(courseStaffMember);

    courseStaffMemberCreatedEvent.fire(new CourseStaffMemberCreatedEvent(courseStaffMember.getId(), 
        courseStaffMember.getCourse().getId(), courseStaffMember.getStaffMember().getId()));

    return courseStaffMember;
  }

  public List<CourseStaffMember> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStaffMember> criteria = criteriaBuilder.createQuery(CourseStaffMember.class);
    Root<CourseStaffMember> root = criteria.from(CourseStaffMember.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseStaffMember_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public CourseStaffMember updateRole(CourseStaffMember courseStaffMember, CourseStaffMemberRole role) {
    courseStaffMember.setRole(role);
    persist(courseStaffMember);
    
    courseStaffMemberUpdatedEvent.fire(new CourseStaffMemberUpdatedEvent(courseStaffMember.getId(), courseStaffMember.getCourse().getId(), courseStaffMember.getStaffMember().getId()));
    
    return courseStaffMember;
  }

  @Override
  public void delete(CourseStaffMember courseStaffMember) {
    Long courseId = courseStaffMember.getCourse().getId();
    Long staffMemberId = courseStaffMember.getStaffMember().getId();
    Long id = courseStaffMember.getId();
    
    super.delete(courseStaffMember);
    
    courseStaffMemberDeletedEvent.fire(new CourseStaffMemberDeletedEvent(id, courseId, staffMemberId));
  }
}

