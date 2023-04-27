package fi.otavanopisto.pyramus.rest.model;

import java.util.Date;

public class CourseActivityAssessment {

  public Long getCourseModuleId() {
    return courseModuleId;
  }

  public void setCourseModuleId(Long courseModuleId) {
    this.courseModuleId = courseModuleId;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public Boolean getPassingGrade() {
    return passingGrade;
  }

  public void setPassingGrade(Boolean passingGrade) {
    this.passingGrade = passingGrade;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public CourseActivityState getState() {
    return state;
  }

  public void setState(CourseActivityState state) {
    this.state = state;
  }

  public Date getGradeDate() {
    return gradeDate;
  }

  public void setGradeDate(Date gradeDate) {
    this.gradeDate = gradeDate;
  }

  private Long courseModuleId;
  private String grade;
  private Boolean passingGrade;
  private Date date;
  private Date gradeDate;
  private String text;
  private CourseActivityState state;

}
