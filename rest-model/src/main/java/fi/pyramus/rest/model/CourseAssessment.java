package fi.pyramus.rest.model;

import org.joda.time.DateTime;

public class CourseAssessment {

  public CourseAssessment() {
    super();
  }

  public CourseAssessment(Long id, Long courseStudentId, Long gradeId, Long assessorId, DateTime date, String verbalAssessment) {
    super();
    this.id = id;
    this.courseStudentId = courseStudentId;
    this.gradeId = gradeId;
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

  public DateTime getDate() {
    return date;
  }

  public void setDate(DateTime date) {
    this.date = date;
  }

  public String getVerbalAssessment() {
    return verbalAssessment;
  }

  public void setVerbalAssessment(String verbalAssessment) {
    this.verbalAssessment = verbalAssessment;
  }

  private Long id;
  private Long courseStudentId;
  private Long gradeId;
  private Long assessorId;
  private DateTime date;
  private String verbalAssessment;
}
