package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.TransferCredit.class, entityType = TranquilModelType.COMPACT)
public class TransferCreditCompact extends TransferCreditBase {

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

  public Long getCourseLength_id() {
    return courseLength_id;
  }

  public void setCourseLength_id(Long courseLength_id) {
    this.courseLength_id = courseLength_id;
  }

  public Long getSchool_id() {
    return school_id;
  }

  public void setSchool_id(Long school_id) {
    this.school_id = school_id;
  }

  public Long getSubject_id() {
    return subject_id;
  }

  public void setSubject_id(Long subject_id) {
    this.subject_id = subject_id;
  }

  private Long grade_id;

  private Long assessingUser_id;

  private Long student_id;

  private Long courseLength_id;

  private Long school_id;

  private Long subject_id;

  public final static String[] properties = {"grade","assessingUser","student","courseLength","school","subject"};
}
