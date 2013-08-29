package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class CourseController {
  @Inject
  CourseDAO courseDAO;
  @Inject
  CourseStateDAO courseStateDAO;
  
  public Course createCourse(Module module, String name, String nameExtension, CourseState state, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User creatingUser) {
    
    Course course = courseDAO
        .create(module, name, nameExtension, state, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit, distanceTeachingDays,
            localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, creatingUser);

    return course;
  }
  
  public List<Course> findCourses() {
    List<Course> courses = courseDAO.listAll();
    return courses;
  }
  
  public List<Course> findUnarchivedCourses() {
    List<Course> courses = courseDAO.listUnarchived();
    return courses;
  }
  
  public Course findCourseById(Long id) {
    Course course = courseDAO.findById(id);
    return course;
  }
  
  public CourseState findCourseStateById(Long id) {
    CourseState state = courseStateDAO.findById(id);
    return state;
  }
  
  public Course updateCourse(Course course, String name, String nameExtension, CourseState courseState, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User user) {
    
    courseDAO.update(course, name, nameExtension, courseState, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit,
        distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, user);
    
    return course;
  }
  
  public Course archiveCourse(Course course, User user) {
    courseDAO.archive(course, user);
    return course;
  }
  
  public Course unarchiveCourse(Course course, User user) {
    courseDAO.unarchive(course, user);
    return course;
  }
}
