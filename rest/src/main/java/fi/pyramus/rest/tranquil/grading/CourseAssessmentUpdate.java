package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CourseAssessment.class, entityType = TranquilModelType.UPDATE)
public class CourseAssessmentUpdate extends CourseAssessmentComplete {

  public void setGrade(GradeCompact grade) {
    super.setGrade(grade);
  }

  public GradeCompact getGrade() {
    return (GradeCompact)super.getGrade();
  }

  public void setAssessingUser(UserCompact assessingUser) {
    super.setAssessingUser(assessingUser);
  }

  public UserCompact getAssessingUser() {
    return (UserCompact)super.getAssessingUser();
  }

  public void setCourseStudent(CourseStudentCompact courseStudent) {
    super.setCourseStudent(courseStudent);
  }

  public CourseStudentCompact getCourseStudent() {
    return (CourseStudentCompact)super.getCourseStudent();
  }

  public final static String[] properties = {"grade","assessingUser","courseStudent"};
}
