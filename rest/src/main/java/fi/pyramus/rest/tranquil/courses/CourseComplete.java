package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.Course.class, entityType = TranquilModelType.COMPLETE)
public class CourseComplete extends CourseBase {

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  public TranquilModelEntity getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(TranquilModelEntity lastModifier) {
    this.lastModifier = lastModifier;
  }

  public TranquilModelEntity getSubject() {
    return subject;
  }

  public void setSubject(TranquilModelEntity subject) {
    this.subject = subject;
  }

  public TranquilModelEntity getCourseEducationTypeByEducationTypeId() {
    return courseEducationTypeByEducationTypeId;
  }

  public void setCourseEducationTypeByEducationTypeId(TranquilModelEntity courseEducationTypeByEducationTypeId) {
    this.courseEducationTypeByEducationTypeId = courseEducationTypeByEducationTypeId;
  }

  public TranquilModelEntity getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(TranquilModelEntity courseLength) {
    this.courseLength = courseLength;
  }

  public TranquilModelEntity getModule() {
    return module;
  }

  public void setModule(TranquilModelEntity module) {
    this.module = module;
  }

  public TranquilModelEntity getState() {
    return state;
  }

  public void setState(TranquilModelEntity state) {
    this.state = state;
  }

  public java.util.List<TranquilModelEntity> getCourseEducationTypes() {
    return courseEducationTypes;
  }

  public void setCourseEducationTypes(java.util.List<TranquilModelEntity> courseEducationTypes) {
    this.courseEducationTypes = courseEducationTypes;
  }

  public java.util.List<TranquilModelEntity> getVariables() {
    return variables;
  }

  public void setVariables(java.util.List<TranquilModelEntity> variables) {
    this.variables = variables;
  }

  public java.util.List<TranquilModelEntity> getCourseComponents() {
    return courseComponents;
  }

  public void setCourseComponents(java.util.List<TranquilModelEntity> courseComponents) {
    this.courseComponents = courseComponents;
  }

  public java.util.List<TranquilModelEntity> getCourseUsers() {
    return courseUsers;
  }

  public void setCourseUsers(java.util.List<TranquilModelEntity> courseUsers) {
    this.courseUsers = courseUsers;
  }

  public java.util.List<TranquilModelEntity> getCourseStudents() {
    return courseStudents;
  }

  public void setCourseStudents(java.util.List<TranquilModelEntity> courseStudents) {
    this.courseStudents = courseStudents;
  }

  public java.util.List<TranquilModelEntity> getStudentCourseResources() {
    return studentCourseResources;
  }

  public void setStudentCourseResources(java.util.List<TranquilModelEntity> studentCourseResources) {
    this.studentCourseResources = studentCourseResources;
  }

  public java.util.List<TranquilModelEntity> getBasicCourseResources() {
    return basicCourseResources;
  }

  public void setBasicCourseResources(java.util.List<TranquilModelEntity> basicCourseResources) {
    this.basicCourseResources = basicCourseResources;
  }

  public java.util.List<TranquilModelEntity> getGradeCourseResources() {
    return gradeCourseResources;
  }

  public void setGradeCourseResources(java.util.List<TranquilModelEntity> gradeCourseResources) {
    this.gradeCourseResources = gradeCourseResources;
  }

  public java.util.List<TranquilModelEntity> getOtherCosts() {
    return otherCosts;
  }

  public void setOtherCosts(java.util.List<TranquilModelEntity> otherCosts) {
    this.otherCosts = otherCosts;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity subject;

  private TranquilModelEntity courseEducationTypeByEducationTypeId;

  private TranquilModelEntity courseLength;

  private TranquilModelEntity module;

  private TranquilModelEntity state;

  private java.util.List<TranquilModelEntity> courseEducationTypes;

  private java.util.List<TranquilModelEntity> variables;

  private java.util.List<TranquilModelEntity> courseComponents;

  private java.util.List<TranquilModelEntity> courseUsers;

  private java.util.List<TranquilModelEntity> courseStudents;

  private java.util.List<TranquilModelEntity> studentCourseResources;

  private java.util.List<TranquilModelEntity> basicCourseResources;

  private java.util.List<TranquilModelEntity> gradeCourseResources;

  private java.util.List<TranquilModelEntity> otherCosts;

  public final static String[] properties = {"creator","lastModifier","subject","courseEducationTypeByEducationTypeId","courseLength","module","state","courseEducationTypes","variables","courseComponents","courseUsers","courseStudents","studentCourseResources","basicCourseResources","gradeCourseResources","otherCosts"};
}
