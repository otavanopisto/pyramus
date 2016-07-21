package fi.otavanopisto.pyramus.rest.model;

import org.threeten.bp.ZonedDateTime;

public class CourseAssessment {

  public CourseAssessment() {
    super();
  }

  public CourseAssessment(Long id, Long courseStudentId, Long gradeId, Long gradingScaleId, Long assessorId, ZonedDateTime date, String verbalAssessment) {
    super();
    this.id = id;
    this.courseStudentId = courseStudentId;
    this.gradeId = gradeId;
    this.gradingScaleId = gradingScaleId;
    this.assessorId = assessorId;
    this.date = date;
    this.verbalAssessment = verbalAssessment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourseStudentId() {
    return courseStudentId;
  }

  public void setCourseStudentId(Long courseStudentId) {
    this.courseStudentId = courseStudentId;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public Long getAssessorId() {
    return assessorId;
  }

  public void setAssessorId(Long assessorId) {
    this.assessorId = assessorId;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public void setDate(ZonedDateTime date) {
    this.date = date;
  }

  public String getVerbalAssessment() {
    return verbalAssessment;
  }

  public void setVerbalAssessment(String verbalAssessment) {
    this.verbalAssessment = verbalAssessment;
  }

  public Long getGradingScaleId() {
    return gradingScaleId;
  }

  public void setGradingScaleId(Long gradingScaleId) {
    this.gradingScaleId = gradingScaleId;
  }

  private Long id;
  private Long courseStudentId;
  private Long gradeId;
  private Long gradingScaleId;
  private Long assessorId;
  private ZonedDateTime date;
  private String verbalAssessment;
}
