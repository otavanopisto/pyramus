package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudentGroup;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudentGroup_;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.events.CourseUpdatedEvent;

@Stateless
public class CourseSignupStudentGroupDAO extends PyramusEntityDAO<CourseSignupStudentGroup> {
  
  @Inject
  private Event<CourseUpdatedEvent> courseUpdatedEvent;

  public CourseSignupStudentGroup create(Course course, StudentGroup studentGroup) {
    CourseSignupStudentGroup courseSignupStudentGroup = new CourseSignupStudentGroup();
    courseSignupStudentGroup.setCourse(course);
    courseSignupStudentGroup.setStudentGroup(studentGroup);
    
    courseSignupStudentGroup = persist(courseSignupStudentGroup);
    
    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));
    
    return courseSignupStudentGroup;
  }
  
  public List<CourseSignupStudentGroup> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseSignupStudentGroup> criteria = criteriaBuilder.createQuery(CourseSignupStudentGroup.class);
    Root<CourseSignupStudentGroup> root = criteria.from(CourseSignupStudentGroup.class);
    
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(CourseSignupStudentGroup_.course), course));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  @Override
  public void delete(CourseSignupStudentGroup courseSignupStudentGroup) {
    Long courseId = courseSignupStudentGroup.getCourse().getId();
    super.delete(courseSignupStudentGroup);
    courseUpdatedEvent.fire(new CourseUpdatedEvent(courseId));
  }
}
