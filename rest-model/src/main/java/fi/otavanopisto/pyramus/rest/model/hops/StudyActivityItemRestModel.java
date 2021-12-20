package fi.otavanopisto.pyramus.rest.model.hops;

import java.util.Date;

public class StudyActivityItemRestModel {

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public StudyActivityItemStatus getStatus() {
    return status;
  }

  public void setStatus(StudyActivityItemStatus status) {
    this.status = status;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  private String subject;
  private Long courseId;
  private Integer courseNumber;
  private String courseName;
  private String grade;
  private StudyActivityItemStatus status;
  private Date date;
}
