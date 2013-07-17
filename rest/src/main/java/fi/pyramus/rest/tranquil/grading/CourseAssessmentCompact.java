package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.CourseAssessment.class, entityType = TranquilModelType.COMPACT)
public class CourseAssessmentCompact extends CourseAssessmentBase {

  public Long getGrade_id() {
    return grade_id;
  }

  public void setGrade_id(Long grade_id) {
    this.grade_id = grade_id;
  }

  public Long getAssessingUser_id() {
    return assessingUser_id;
  }

  public void setAssessingUser_id(Long assessingUser_id) {
    this.assessingUser_id = assessingUser_id;
  }

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  public Long getCourseStudent_id() {
    return courseStudent_id;
  }

  public void setCourseStudent_id(Long courseStudent_id) {
    this.courseStudent_id = courseStudent_id;
  }

  private Long grade_id;

  private Long assessingUser_id;

  private Long student_id;

  private Long courseStudent_id;

  public final static String[] properties = {"grade","assessingUser","student","courseStudent"};
}
