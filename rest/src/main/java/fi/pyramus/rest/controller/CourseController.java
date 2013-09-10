package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.courses.CourseComponentDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class CourseController {
  @Inject
  private CourseDAO courseDAO;
  @Inject
  private CourseStateDAO courseStateDAO;
  @Inject
  private TagDAO tagDAO;
  @Inject
  private CourseDescriptionCategoryDAO courseDescriptionCategoryDAO;
  @Inject
  private CourseParticipationTypeDAO courseParticipationTypeDAO;
  @Inject
  private CourseComponentDAO courseComponentDAO;
  
  public Course createCourse(Module module, String name, String nameExtension, CourseState state, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User creatingUser) {
    
    Course course = courseDAO
        .create(module, name, nameExtension, state, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit, distanceTeachingDays,
            localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, creatingUser);

    return course;
  }
  
  public CourseComponent createCourseComponent(Course course, Double componentLength, EducationalTimeUnit componentLengthTimeUnit, String name, String description) {
    CourseComponent courseComponent = courseComponentDAO.create(course, componentLength, componentLengthTimeUnit, name, description);
    return courseComponent;
  }
  
  public CourseDescriptionCategory createCourseDescriptionCategory(String name) {
    CourseDescriptionCategory courseDescriptionCategory = courseDescriptionCategoryDAO.create(name);
    return courseDescriptionCategory;
  }
  
  public CourseState createCourseState(String name) {
    CourseState courseState = courseStateDAO.create(name);
    return courseState;
  }
  
  public CourseParticipationType createCourseParticipationType(String name) {
    CourseParticipationType courseParticipationType = courseParticipationTypeDAO.create(name);
    return courseParticipationType;
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
  
  public List<CourseComponent> findCourseComponentsByCourse(Course course) {
    List<CourseComponent> components = courseComponentDAO.listByCourse(course);
    return components;
  }
  
  public List<CourseComponent> findUnarchivedCourseComponents() {
    List<CourseComponent> components = courseComponentDAO.listUnarchived();
    return components;
  }
  
  public CourseComponent findCourseComponentById(Long id) {
    CourseComponent component = courseComponentDAO.findById(id);
    return component;
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
  
  public List<CourseParticipationType> findCourseParticiPationTypes() {
    List<CourseParticipationType> courseParticipationTypes = courseParticipationTypeDAO.listAll();
    return courseParticipationTypes;
  }
  
  public List<CourseParticipationType> findUnarchivedCourseParticiPationTypes() {
    List<CourseParticipationType> courseParticipationTypes = courseParticipationTypeDAO.listUnarchived();
    return courseParticipationTypes;
  }
  
  public CourseParticipationType findCourseParticipationTypeById(Long id) {
    CourseParticipationType courseParticipationType = courseParticipationTypeDAO.findById(id);
    return courseParticipationType;
  }
  
  public Course updateCourse(Course course, String name, String nameExtension, CourseState courseState, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User user) {
    
    courseDAO.update(course, name, nameExtension, courseState, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit,
        distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, user);
    
    return course;
  }
  
  public CourseComponent updateCourseComponent(CourseComponent component, Double length, EducationalTimeUnit lengthTimeUnit, String name, String description) {
    CourseComponent updated = courseComponentDAO.update(component, length, lengthTimeUnit, name, description);
    return updated;
  }
  
  public CourseDescriptionCategory updateCourseDescriptionCategory(CourseDescriptionCategory courseDescriptionCategory, String name) {
    CourseDescriptionCategory updated = courseDescriptionCategoryDAO.update(courseDescriptionCategory, name);
    return updated;
  }
  
  public CourseState updateCourseState(CourseState courseState, String name) {
    CourseState updated = courseStateDAO.update(courseState, name);
    return updated;
  }
  
  public CourseParticipationType updateCourseParticipationType(CourseParticipationType courseParticipationType, String name) {
    CourseParticipationType updated = courseParticipationTypeDAO.update(courseParticipationType, name);
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
  
  public CourseComponent archiveCourseComponent(CourseComponent courseComponent, User user) {
    courseComponentDAO.archive(courseComponent, user);
    return courseComponent;
  }
  
  public CourseComponent unarchiveCourseComponent(CourseComponent courseComponent, User user) {
    courseComponentDAO.unarchive(courseComponent, user);
    return courseComponent;
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
  
  public CourseParticipationType archiveCourseParticipationType(CourseParticipationType courseParticipationType, User user) {
    courseParticipationTypeDAO.archive(courseParticipationType, user);
    return courseParticipationType;
  }
  
  public CourseParticipationType unarchiveCourseParticipationType(CourseParticipationType courseParticipationType, User user) {
    courseParticipationTypeDAO.unarchive(courseParticipationType, user);
    return courseParticipationType;
  }
  
  public void removeCourseTag(Course course, Tag tag) {
    course.removeTag(tag);
  }
}
