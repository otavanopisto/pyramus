package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
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
  @Inject
  TagDAO tagDAO;
  @Inject
  CourseDescriptionCategoryDAO courseDescriptionCategoryDAO;
  
  public Course createCourse(Module module, String name, String nameExtension, CourseState state, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User creatingUser) {
    
    Course course = courseDAO
        .create(module, name, nameExtension, state, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit, distanceTeachingDays,
            localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, creatingUser);

    return course;
  }
  
  public CourseDescriptionCategory createCourseDescriptionCategory(String name) {
    CourseDescriptionCategory courseDescriptionCategory = courseDescriptionCategoryDAO.create(name);
    return courseDescriptionCategory;
  }
  
  public CourseState createCourseState(String name) {
    CourseState courseState = courseStateDAO.create(name);
    return courseState;
  }
  
  public Tag createCourseTag(Course course, String text) {
    Tag tag = tagDAO.findByText(text);
    if(tag == null) {
      tag = tagDAO.create(text);
    }
    course.addTag(tag);
    return tag;
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
  
  public List<CourseDescriptionCategory> findCourseDescriptionCategories() {
    List<CourseDescriptionCategory> courseDescriptionCategories = courseDescriptionCategoryDAO.listAll();
    return courseDescriptionCategories;
  }

  public List<CourseDescriptionCategory> findUnarchivedCourseDescriptionCategories() {
    List<CourseDescriptionCategory> courseDescriptionCategories = courseDescriptionCategoryDAO.listUnarchived();
    return courseDescriptionCategories;
  }
  
  public CourseDescriptionCategory findCourseDescriptionCategoryById(Long id) {
    CourseDescriptionCategory courseDescriptionCategory = courseDescriptionCategoryDAO.findById(id);
    return courseDescriptionCategory;
  }
  
  public List<CourseState> findCourseStates() {
    List<CourseState> courseStates = courseStateDAO.listAll();
    return courseStates;
  }
  
  public List<CourseState> findUnarchivedCourseStates() {
    List<CourseState> courseStates = courseStateDAO.listUnarchived();
    return courseStates;
  }
  
  public CourseState findCourseStateById(Long id) {
    CourseState state = courseStateDAO.findById(id);
    return state;
  }
  
  public Set<Tag> findCourseTags(Course course) {
    Set<Tag> tags= course.getTags();
    return tags;
  }
  
  public Course updateCourse(Course course, String name, String nameExtension, CourseState courseState, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User user) {
    
    courseDAO.update(course, name, nameExtension, courseState, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit,
        distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, user);
    
    return course;
  }
  
  public CourseDescriptionCategory updateCourseDescriptionCategory(CourseDescriptionCategory courseDescriptionCategory, String name) {
    CourseDescriptionCategory updated = courseDescriptionCategoryDAO.update(courseDescriptionCategory, name);
    return updated;
  }
  
  public CourseState updateCourseState(CourseState courseState, String name) {
    CourseState updated = courseStateDAO.update(courseState, name);
    return updated;
  }
  
  public Course archiveCourse(Course course, User user) {
    courseDAO.archive(course, user);
    return course;
  }
  
  public Course unarchiveCourse(Course course, User user) {
    courseDAO.unarchive(course, user);
    return course;
  }
  
  public CourseDescriptionCategory archiveCourseDescriptionCategory(CourseDescriptionCategory courseDescriptionCategory, User user) {
    courseDescriptionCategoryDAO.archive(courseDescriptionCategory, user);
    return courseDescriptionCategory;
  }
  
  public CourseDescriptionCategory unarchiveCourseDescriptionCategory(CourseDescriptionCategory courseDescriptionCategory, User user) {
    courseDescriptionCategoryDAO.unarchive(courseDescriptionCategory, user);
    return courseDescriptionCategory;
  }
  
  public CourseState archiveCourseState(CourseState courseState, User user) {
    courseStateDAO.archive(courseState, user);
    return courseState;
  }
  
  public CourseState unarchiveCourseState(CourseState courseState, User user) {
    courseStateDAO.unarchive(courseState, user);
    return courseState;
  }
  
  public void removeCourseTag(Course course, Tag tag) {
    course.removeTag(tag);
  }
}
