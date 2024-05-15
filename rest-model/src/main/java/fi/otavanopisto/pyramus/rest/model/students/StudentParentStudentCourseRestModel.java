package fi.otavanopisto.pyramus.rest.model.students;

import java.util.Date;

public class StudentParentStudentCourseRestModel {

  public StudentParentStudentCourseRestModel() {
  }
  
  public StudentParentStudentCourseRestModel(Long courseId, Long courseStudentId, String name, String nameExtension, Date enrolmentDate,
      Date latestAssessmentRequestDate) {
    this.courseId = courseId;
    this.courseStudentId = courseStudentId;
    this.name = name;
    this.nameExtension = nameExtension;
    this.enrolmentDate = enrolmentDate;
    this.latestAssessmentRequestDate = latestAssessmentRequestDate;
  }

  public Long getCourseId() {
    return courseId;
  }
  
  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }
  
  public Long getCourseStudentId() {
    return courseStudentId;
  }
  
  public void setCourseStudentId(Long courseStudentId) {
    this.courseStudentId = courseStudentId;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getNameExtension() {
    return nameExtension;
  }
  
  public void setNameExtension(String nameExtension) {
    this.nameExtension = nameExtension;
  }
  
  public Date getEnrolmentDate() {
    return enrolmentDate;
  }
  
  public void setEnrolmentDate(Date enrolmentDate) {
    this.enrolmentDate = enrolmentDate;
  }
  
  public Date getLatestAssessmentRequestDate() {
    return latestAssessmentRequestDate;
  }
  
  public void setLatestAssessmentRequestDate(Date latestAssessmentRequestDate) {
    this.latestAssessmentRequestDate = latestAssessmentRequestDate;
  }

  private Long courseId;
  private Long courseStudentId;
  private String name;
  private String nameExtension;
  private Date enrolmentDate;
  private Date latestAssessmentRequestDate;
}
