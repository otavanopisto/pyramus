package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.Course.class, entityType = TranquilModelType.UPDATE)
public class CourseUpdate extends CourseComplete {

  public void setCreator(UserCompact creator) {
    super.setCreator(creator);
  }

  public UserCompact getCreator() {
    return (UserCompact)super.getCreator();
  }

  public void setLastModifier(UserCompact lastModifier) {
    super.setLastModifier(lastModifier);
  }

  public UserCompact getLastModifier() {
    return (UserCompact)super.getLastModifier();
  }

  public void setSubject(SubjectCompact subject) {
    super.setSubject(subject);
  }

  public SubjectCompact getSubject() {
    return (SubjectCompact)super.getSubject();
  }

  public void setCourseEducationTypeByEducationTypeId(CourseEducationTypeCompact courseEducationTypeByEducationTypeId) {
    super.setCourseEducationTypeByEducationTypeId(courseEducationTypeByEducationTypeId);
  }

  public CourseEducationTypeCompact getCourseEducationTypeByEducationTypeId() {
    return (CourseEducationTypeCompact)super.getCourseEducationTypeByEducationTypeId();
  }

  public void setCourseLength(EducationalLengthCompact courseLength) {
    super.setCourseLength(courseLength);
  }

  public EducationalLengthCompact getCourseLength() {
    return (EducationalLengthCompact)super.getCourseLength();
  }

  public void setModule(ModuleCompact module) {
    super.setModule(module);
  }

  public ModuleCompact getModule() {
    return (ModuleCompact)super.getModule();
  }

  public void setState(CourseStateCompact state) {
    super.setState(state);
  }

  public CourseStateCompact getState() {
    return (CourseStateCompact)super.getState();
  }

  public void setCourseEducationTypes(java.util.List<CourseEducationTypeCompact> courseEducationTypes) {
    super.setCourseEducationTypes(courseEducationTypes);
  }

  public java.util.List<CourseEducationTypeCompact> getCourseEducationTypes() {
    return (java.util.List<CourseEducationTypeCompact>)super.getCourseEducationTypes();
  }

  public void setVariables(java.util.List<CourseBaseVariableCompact> variables) {
    super.setVariables(variables);
  }

  public java.util.List<CourseBaseVariableCompact> getVariables() {
    return (java.util.List<CourseBaseVariableCompact>)super.getVariables();
  }

  public void setCourseComponents(java.util.List<CourseComponentCompact> courseComponents) {
    super.setCourseComponents(courseComponents);
  }

  public java.util.List<CourseComponentCompact> getCourseComponents() {
    return (java.util.List<CourseComponentCompact>)super.getCourseComponents();
  }

  public void setCourseUsers(java.util.List<CourseUserCompact> courseUsers) {
    super.setCourseUsers(courseUsers);
  }

  public java.util.List<CourseUserCompact> getCourseUsers() {
    return (java.util.List<CourseUserCompact>)super.getCourseUsers();
  }

  public void setCourseStudents(java.util.List<CourseStudentCompact> courseStudents) {
    super.setCourseStudents(courseStudents);
  }

  public java.util.List<CourseStudentCompact> getCourseStudents() {
    return (java.util.List<CourseStudentCompact>)super.getCourseStudents();
  }

  public void setStudentCourseResources(java.util.List<StudentCourseResourceCompact> studentCourseResources) {
    super.setStudentCourseResources(studentCourseResources);
  }

  public java.util.List<StudentCourseResourceCompact> getStudentCourseResources() {
    return (java.util.List<StudentCourseResourceCompact>)super.getStudentCourseResources();
  }

  public void setBasicCourseResources(java.util.List<BasicCourseResourceCompact> basicCourseResources) {
    super.setBasicCourseResources(basicCourseResources);
  }

  public java.util.List<BasicCourseResourceCompact> getBasicCourseResources() {
    return (java.util.List<BasicCourseResourceCompact>)super.getBasicCourseResources();
  }

  public void setGradeCourseResources(java.util.List<GradeCourseResourceCompact> gradeCourseResources) {
    super.setGradeCourseResources(gradeCourseResources);
  }

  public java.util.List<GradeCourseResourceCompact> getGradeCourseResources() {
    return (java.util.List<GradeCourseResourceCompact>)super.getGradeCourseResources();
  }

  public void setOtherCosts(java.util.List<OtherCostCompact> otherCosts) {
    super.setOtherCosts(otherCosts);
  }

  public java.util.List<OtherCostCompact> getOtherCosts() {
    return (java.util.List<OtherCostCompact>)super.getOtherCosts();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public final static String[] properties = {"creator","lastModifier","subject","courseEducationTypeByEducationTypeId","courseLength","module","state","courseEducationTypes","variables","courseComponents","courseUsers","courseStudents","studentCourseResources","basicCourseResources","gradeCourseResources","otherCosts","tags"};
}
