package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.CourseBaseVariableDAO;
import fi.pyramus.dao.base.CourseBaseVariableKeyDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.courses.CourseComponentDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.courses.CourseTypeDAO;
import fi.pyramus.domainmodel.base.BillingDetails;
import fi.pyramus.domainmodel.base.CourseBaseVariable;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStaffMember;
import fi.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.CourseType;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class CourseController {
  
  @Inject
  private CourseDAO courseDAO;
  
  @Inject
  private CourseStateDAO courseStateDAO;
  
  @Inject
  private CourseTypeDAO courseTypeDAO;
  
  @Inject
  private TagDAO tagDAO;
  
  @Inject
  private CourseDescriptionCategoryDAO courseDescriptionCategoryDAO;
  
  @Inject
  private CourseParticipationTypeDAO courseParticipationTypeDAO;
  
  @Inject
  private CourseComponentDAO courseComponentDAO;
  
  @Inject
  private CourseEnrolmentTypeDAO courseEnrolmentTypeDAO;

  @Inject
  private CourseStaffMemberRoleDAO courseStaffMemberRoleDAO;

  @Inject
  private CourseStaffMemberDAO courseStaffMemberDAO;

  @Inject
  private CourseStudentDAO courseStudentDAO;

  @Inject
  private CourseBaseVariableDAO courseBaseVariableDAO;

  @Inject
  private CourseBaseVariableKeyDAO courseBaseVariableKeyDAO;
  
  @Inject
  private DefaultsDAO defaultsDAO;
  
  public Course createCourse(Module module, String name, String nameExtension, CourseState state, CourseType type, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User creatingUser) {
    
    Course course = courseDAO
        .create(module, name, nameExtension, state, type, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit, distanceTeachingDays,
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
  
  public CourseEnrolmentType createCourseEnrolmentType(String name) {
    CourseEnrolmentType enrolmentType = courseEnrolmentTypeDAO.create(name);
    return enrolmentType;
  }
  
  public CourseState createCourseState(String name) {
    CourseState courseState = courseStateDAO.create(name);
    return courseState;
  }
  
  public CourseParticipationType createCourseParticipationType(String name) {
    CourseParticipationType courseParticipationType = courseParticipationTypeDAO.create(name);
    return courseParticipationType;
  }
  
  public synchronized Tag createCourseTag(Course course, String text) {
    Tag tag = tagDAO.findByText(text);
    if (tag == null) {
      tag = tagDAO.create(text);
    }

    course.addTag(tag);
    return tag;
  }
  
  public synchronized Course updateCourseTags(Course course, List<String> tags) {
    Set<String> newTags = new HashSet<>(tags);
    Set<Tag> courseTags = new HashSet<>(course.getTags());
    
    for (Tag courseTag : courseTags) {
      if (!newTags.contains(courseTag.getText())) {
        removeCourseTag(course, courseTag);
      }
        
      newTags.remove(courseTag.getText());
    }
    
    for (String newTag : newTags) {
      createCourseTag(course, newTag);
    }
    
    return course;
  }
  
  public List<Course> listCourses() {
    return courseDAO.listAll();
  }

  public List<Course> listCourses(Integer firstResult, Integer maxResults) {
    return courseDAO.listAll(firstResult, maxResults);
  }
  
  public List<Course> listUnarchivedCourses() {
    return courseDAO.listUnarchived();
  }

  public List<Course> listUnarchivedCourses(Integer firstResult, Integer maxResults) {
    return courseDAO.listUnarchived(firstResult, maxResults);
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
  
  public List<CourseEnrolmentType> listCourseEnrolmentTypes() {
    List<CourseEnrolmentType> enrolmentTypes = courseEnrolmentTypeDAO.listAll();
    return enrolmentTypes;
  }
  
  public CourseEnrolmentType findCourseEnrolmentTypeById(Long id) {
    CourseEnrolmentType enrolmentType = courseEnrolmentTypeDAO.findById(id);
    return enrolmentType;
  }

  public void deleteCourseEnrolmentType(fi.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType) {
    courseEnrolmentTypeDAO.delete(enrolmentType);
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
  
  public CourseType findCourseTypeById(Long id) {
    return courseTypeDAO.findById(id);
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
  
  public Course updateCourse(Course course, String name, String nameExtension, CourseState courseState, CourseType type, Subject subject, Integer courseNumber, Date beginDate,
      Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours,
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User user) {
    
    courseDAO.update(course, name, nameExtension, courseState, type, subject, courseNumber, beginDate, endDate, courseLength, courseLengthTimeUnit,
        distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, user);
    
    return course;
  }
  
  public CourseComponent updateCourseComponent(CourseComponent component, Double length, EducationalTimeUnit lengthTimeUnit, String name, String description) {
    CourseComponent updated = courseComponentDAO.update(component, length, lengthTimeUnit, name, description);
    return updated;
  }
  
  public CourseEnrolmentType updateCourseEnrolmentType(CourseEnrolmentType courseEnrolmentType, String name) {
    CourseEnrolmentType updated = courseEnrolmentTypeDAO.updateName(courseEnrolmentType, name);
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

  public void deleteCourse(Course course) {
    Set<Tag> tags = new HashSet<>(course.getTags());
    for (Tag tag : tags) {
      removeCourseTag(course, tag);
    }
    
    courseDAO.delete(course);
  }
  
  public CourseComponent archiveCourseComponent(CourseComponent courseComponent, User user) {
    courseComponentDAO.archive(courseComponent, user);
    return courseComponent;
  }
  
  public CourseComponent unarchiveCourseComponent(CourseComponent courseComponent, User user) {
    courseComponentDAO.unarchive(courseComponent, user);
    return courseComponent;
  }

  public void deleteCourseComponent(CourseComponent courseComponent) {
    courseComponentDAO.delete(courseComponent);
  }
  
  public CourseDescriptionCategory archiveCourseDescriptionCategory(CourseDescriptionCategory courseDescriptionCategory, User user) {
    courseDescriptionCategoryDAO.archive(courseDescriptionCategory, user);
    return courseDescriptionCategory;
  }
  
  public CourseDescriptionCategory unarchiveCourseDescriptionCategory(CourseDescriptionCategory courseDescriptionCategory, User user) {
    courseDescriptionCategoryDAO.unarchive(courseDescriptionCategory, user);
    return courseDescriptionCategory;
  }

  public void deleteCourseDescriptionCategory(CourseDescriptionCategory category) {
    courseDescriptionCategoryDAO.delete(category);;
  }
  
  public CourseState archiveCourseState(CourseState courseState, User user) {
    courseStateDAO.archive(courseState, user);
    return courseState;
  }
  
  public CourseState unarchiveCourseState(CourseState courseState, User user) {
    courseStateDAO.unarchive(courseState, user);
    return courseState;
  }

  public void deleteCourseState(CourseState courseState) {
    courseStateDAO.delete(courseState);
  }
  
  public CourseParticipationType archiveCourseParticipationType(CourseParticipationType courseParticipationType, User user) {
    courseParticipationTypeDAO.archive(courseParticipationType, user);
    return courseParticipationType;
  }
  
  public CourseParticipationType unarchiveCourseParticipationType(CourseParticipationType courseParticipationType, User user) {
    courseParticipationTypeDAO.unarchive(courseParticipationType, user);
    return courseParticipationType;
  }

  public void deleteCourseParticipationType(CourseParticipationType participationType) {
    courseParticipationTypeDAO.delete(participationType);
  }
  
  public void removeCourseTag(Course course, Tag tag) {
    course.removeTag(tag);
  }

  public List<Course> listCoursesBySubject(Subject subject) {
    return courseDAO.listBySubject(subject);
  }
  
  /* CourseStaffMemberRole */

  public CourseStaffMemberRole createStaffMemberRole(String name) {
    return courseStaffMemberRoleDAO.create(name);
  }
  
  public CourseStaffMemberRole findStaffMemberRoleById(Long id) {
    return courseStaffMemberRoleDAO.findById(id);
  }
  
  public List<CourseStaffMemberRole> listStaffMemberRoles() {
    return courseStaffMemberRoleDAO.listAll();
  }

  public CourseStaffMemberRole updateCourseStaffMemberRoleName(CourseStaffMemberRole courseStaffMemberRole, String name) {
    return courseStaffMemberRoleDAO.updateName(courseStaffMemberRole, name);
  }
  
  public void deleteStaffMemberRole(CourseStaffMemberRole courseStaffMemberRole) {
    courseStaffMemberRoleDAO.delete(courseStaffMemberRole);
  }
  
  /* CourseStaffMembers */
  
  public CourseStaffMember createStaffMember(Course course, StaffMember staffMember, CourseStaffMemberRole role) {
    return courseStaffMemberDAO.create(course, staffMember, role);
  }
  
  public CourseStaffMember findStaffMemberById(Long id) {
    return courseStaffMemberDAO.findById(id);
  }
  
  public List<CourseStaffMember> listStaffMembersByCourse(Course course) {
    return courseStaffMemberDAO.listByCourse(course);
  }

  public CourseStaffMember updateStaffMemberRole(CourseStaffMember staffMember, CourseStaffMemberRole role) {
    return courseStaffMemberDAO.updateRole(staffMember, role);
  }
  
  public void deleteStaffMember(CourseStaffMember courseStaffMember) {
    courseStaffMemberDAO.delete(courseStaffMember);
  }
  
  /* CourseStudent */
  
  public CourseStudent createCourseStudent(Course course, Student student, CourseEnrolmentType enrolmentType, CourseParticipationType participationType, Date enrolmentDate, Boolean lodging, CourseOptionality optionality, BillingDetails billingDetails) {
    return courseStudentDAO.create(course, student, enrolmentType, participationType, enrolmentDate, lodging, optionality, billingDetails, Boolean.FALSE);
  }
  
  public CourseStudent findCourseStudentById(Long id) {
    return courseStudentDAO.findById(id);
  }
  
  public List<CourseStudent> listCourseStudentsByCourse(Course course) {
    return courseStudentDAO.listByCourse(course);
  }

  public List<CourseStudent> listCourseStudentsByStudent(Student student) {
    return courseStudentDAO.listByStudent(student);
  }

  public CourseStudent updateCourseStudent(CourseStudent courseStudent, Boolean lodging, BillingDetails billingDetails, CourseEnrolmentType enrolmentType, Date enrolmentTime, CourseOptionality optionality, CourseParticipationType participationType) {
    courseStudentDAO.updateLodging(courseStudent, lodging);
    courseStudentDAO.updateBillingDetails(courseStudent, billingDetails);
    courseStudentDAO.updateEnrolmentType(courseStudent, enrolmentType);
    courseStudentDAO.updateEnrolmentTime(courseStudent, enrolmentTime);
    courseStudentDAO.updateOptionality(courseStudent, optionality);
    courseStudentDAO.updateParticipationType(courseStudent, participationType);
    return courseStudent;
  }

  public CourseStudent archiveCourseStudent(CourseStudent courseStudent, User loggedUser) {
    courseStudentDAO.archive(courseStudent, loggedUser);
    return courseStudent;
  }

  public void deleteCourseStudent(CourseStudent courseStudent) {
    courseStudentDAO.delete(courseStudent);
  }

  public CourseEnrolmentType getDefaultCourseEnrolmentType() {
    return defaultsDAO.getDefaults().getInitialCourseEnrolmentType();
  }

  public CourseParticipationType getDefaultCourseParticipationType() {
    return defaultsDAO.getDefaults().getInitialCourseParticipationType();
  }

  /* Variables */

  public Course updateCourseVariables(Course course, Map<String, String> variables) {
    Set<String> newKeys = new HashSet<>(variables.keySet());
    Set<String> oldKeys = new HashSet<>();
    Set<String> updateKeys = new HashSet<>();
    
    List<CourseBaseVariable> courseVariables = courseBaseVariableDAO.listByCourseBase(course);
    
    for (CourseBaseVariable variable : courseVariables) {
      oldKeys.add(variable.getKey().getVariableKey());
    }

    for (String oldKey : oldKeys) {
      if (!newKeys.contains(oldKey)) {
        CourseBaseVariableKey key = findCourseBaseVariableKeyByVariableKey(oldKey);
        CourseBaseVariable courseVariable = findCourseVariableByCourseAndKey(course, key);
        deleteCourseVariable(courseVariable);
      } else {
        updateKeys.add(oldKey);
      }
      
      newKeys.remove(oldKey);
    }
    
    for (String newKey : newKeys) {
      String value = variables.get(newKey);
      CourseBaseVariableKey key = findCourseBaseVariableKeyByVariableKey(newKey);
      createCourseVariable(course, key, value);
    }
    
    for (String updateKey : updateKeys) {
      String value = variables.get(updateKey);
      CourseBaseVariableKey key = findCourseBaseVariableKeyByVariableKey(updateKey);
      CourseBaseVariable courseVariable = findCourseVariableByCourseAndKey(course, key);
      updateCourseVariable(courseVariable, value);
    }
    
    return course;
  }
  
  public CourseBaseVariable createCourseVariable(Course course, CourseBaseVariableKey key, String value) {
    return courseBaseVariableDAO.create(course, key, value);
  }

  public CourseBaseVariable findCourseVariableById(Long id) {
    return courseBaseVariableDAO.findById(id);
  }

  public CourseBaseVariable findCourseVariableByCourseAndKey(Course course, CourseBaseVariableKey key) {
    return courseBaseVariableDAO.findByCourseAndVariableKey(course, key);
  }

  public void deleteCourseVariable(CourseBaseVariable variable) {
    courseBaseVariableDAO.delete(variable);
  }

  public CourseBaseVariable updateCourseVariable(CourseBaseVariable variable, String value) {
    return courseBaseVariableDAO.update(variable, value);
  }
  
  public CourseBaseVariableKey findCourseBaseVariableKeyByVariableKey(String variableKey) {
    return courseBaseVariableKeyDAO.findByVariableKey(variableKey);
  }

  public List<CourseBaseVariable> listCourseVariablesByCourse(Course course) {
    return courseBaseVariableDAO.listByCourseBase(course);
  }
  
  /* Variable Keys */

  public CourseBaseVariableKey createCourseVariableKey(String key, String name, VariableType variableType, Boolean userEditable) {
    return courseBaseVariableKeyDAO.create(key, name, variableType, userEditable);
  }

  public List<CourseBaseVariableKey> listCourseVariableKeys() {
    return courseBaseVariableKeyDAO.listAll();
  }

  public CourseBaseVariableKey updateCourseVariableKey(CourseBaseVariableKey courseVariableKey, String name, VariableType variableType, Boolean userEditable) {
    courseBaseVariableKeyDAO.updateVariableName(courseVariableKey, name);
    courseBaseVariableKeyDAO.updateVariableType(courseVariableKey, variableType);
    courseBaseVariableKeyDAO.updateUserEditable(courseVariableKey, userEditable);
    return courseVariableKey;
  }

  public void deleteCourseVariableKey(CourseBaseVariableKey courseVariableKey) {
    courseBaseVariableKeyDAO.delete(courseVariableKey);
  }
  
}
