package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudyProgramme_;
import fi.otavanopisto.pyramus.events.CourseUpdatedEvent;

@Stateless
public class CourseSignupStudyProgrammeDAO extends PyramusEntityDAO<CourseSignupStudyProgramme> {
  
  @Inject
  private Event<CourseUpdatedEvent> courseUpdatedEvent;

  public CourseSignupStudyProgramme create(Course course, StudyProgramme studyProgramme) {
    CourseSignupStudyProgramme courseSignupStudyProgramme = new CourseSignupStudyProgramme();
    courseSignupStudyProgramme.setCourse(course);
    courseSignupStudyProgramme.setStudyProgramme(studyProgramme);
    
    courseSignupStudyProgramme = persist(courseSignupStudyProgramme);
    
    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));
    
    return courseSignupStudyProgramme;
  }
  
  public List<CourseSignupStudyProgramme> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseSignupStudyProgramme> criteria = criteriaBuilder.createQuery(CourseSignupStudyProgramme.class);
    Root<CourseSignupStudyProgramme> root = criteria.from(CourseSignupStudyProgramme.class);
    
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(CourseSignupStudyProgramme_.course), course));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  @Override
  public void delete(CourseSignupStudyProgramme courseSignupStudyProgramme) {
    Long courseId = courseSignupStudyProgramme.getCourse().getId();
    super.delete(courseSignupStudyProgramme);
    courseUpdatedEvent.fire(new CourseUpdatedEvent(courseId));
  }
}
