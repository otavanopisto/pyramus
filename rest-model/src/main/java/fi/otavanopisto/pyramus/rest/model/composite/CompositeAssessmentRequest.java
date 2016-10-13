package fi.otavanopisto.pyramus.rest.model.composite;

import java.util.Date;

public class CompositeAssessmentRequest {

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getStudyProgramme() {
    return studyProgramme;
  }

  public void setStudyProgramme(String studyProgramme) {
    this.studyProgramme = studyProgramme;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Date getCourseEnrollmentDate() {
    return courseEnrollmentDate;
  }

  public void setCourseEnrollmentDate(Date courseEnrollmentDate) {
    this.courseEnrollmentDate = courseEnrollmentDate;
  }

  public Date getAssessmentRequestDate() {
    return assessmentRequestDate;
  }

  public void setAssessmentRequestDate(Date assessmentRequestDate) {
    this.assessmentRequestDate = assessmentRequestDate;
  }

  public String getCourseNameExtension() {
    return courseNameExtension;
  }

  public void setCourseNameExtension(String courseNameExtension) {
    this.courseNameExtension = courseNameExtension;
  }

  public Date getEvaluationDate() {
    return evaluationDate;
  }

  public void setEvaluationDate(Date evaluationDate) {
    this.evaluationDate = evaluationDate;
  }

  public Boolean getPassing() {
    return passing;
  }

  public void setPassing(Boolean passing) {
    this.passing = passing;
  }

  private Long userId;
  private String firstName;
  private String lastName;
  private String studyProgramme;
  private Long courseId;
  private String courseName;
  private String courseNameExtension;
  private Date courseEnrollmentDate;
  private Date assessmentRequestDate;
  private Date evaluationDate;
  private Boolean passing;

}
