package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;

public class CourseAssessment {

  public CourseAssessment() {
    super();
  }

  public CourseAssessment(Long id, Long courseStudentId, Long gradeId, Long gradingScaleId, Long assessorId, OffsetDateTime date, String verbalAssessment, Boolean passing) {
    super();
    this.id = id;
    this.courseStudentId = courseStudentId;
    this.gradeId = gradeId;
    this.gradingScaleId = gradingScaleId;
    this.assessorId = assessorId;
    this.date = date;
    this.verbalAssessment = verbalAssessment;
    this.passing = passing;
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

  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
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

  public Boolean getPassing() {
    return passing;
  }

  public void setPassing(Boolean passing) {
    this.passing = passing;
  }

  private Long id;
  private Long courseStudentId;
  private Long gradeId;
  private Long gradingScaleId;
  private Long assessorId;
  private OffsetDateTime date;
  private String verbalAssessment;
  private Boolean passing;
}
