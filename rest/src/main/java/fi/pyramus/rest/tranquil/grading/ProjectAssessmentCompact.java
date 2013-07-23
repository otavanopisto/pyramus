package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.ProjectAssessment.class, entityType = TranquilModelType.COMPACT)
public class ProjectAssessmentCompact extends ProjectAssessmentBase {

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

  public Long getStudentProject_id() {
    return studentProject_id;
  }

  public void setStudentProject_id(Long studentProject_id) {
    this.studentProject_id = studentProject_id;
  }

  private Long grade_id;

  private Long assessingUser_id;

  private Long studentProject_id;

  public final static String[] properties = {"grade","assessingUser","studentProject"};
}
