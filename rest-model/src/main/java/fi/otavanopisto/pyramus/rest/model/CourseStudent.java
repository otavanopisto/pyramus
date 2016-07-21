package fi.otavanopisto.pyramus.rest.model;

import org.threeten.bp.ZonedDateTime;

import fi.otavanopisto.security.ContextReference;

public class CourseStudent implements ContextReference {

  public CourseStudent() {
    super();
  }

  public CourseStudent(Long id, Long courseId, Long studentId, ZonedDateTime enrolmentTime, Boolean archived, Long participationTypeId, Long enrolmentTypeId,
      Boolean lodging, CourseOptionality optionality, Long billingDetailsId) {
    super();
    this.id = id;
    this.courseId = courseId;
    this.studentId = studentId;
    this.enrolmentTime = enrolmentTime;
    this.archived = archived;
    this.participationTypeId = participationTypeId;
    this.enrolmentTypeId = enrolmentTypeId;
    this.lodging = lodging;
    this.optionality = optionality;
    this.billingDetailsId = billingDetailsId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public ZonedDateTime getEnrolmentTime() {
    return enrolmentTime;
  }

  public void setEnrolmentTime(ZonedDateTime enrolmentTime) {
    this.enrolmentTime = enrolmentTime;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getParticipationTypeId() {
    return participationTypeId;
  }

  public void setParticipationTypeId(Long participationTypeId) {
    this.participationTypeId = participationTypeId;
  }
  
  public Long getEnrolmentTypeId() {
    return enrolmentTypeId;
  }
  
  public void setEnrolmentTypeId(Long enrolmentTypeId) {
    this.enrolmentTypeId = enrolmentTypeId;
  }

  public Boolean getLodging() {
    return lodging;
  }

  public void setLodging(Boolean lodging) {
    this.lodging = lodging;
  }

  public CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public Long getBillingDetailsId() {
    return billingDetailsId;
  }

  public void setBillingDetailsId(Long billingDetailsId) {
    this.billingDetailsId = billingDetailsId;
  }

  private Long id;
  private Long courseId;
  private Long studentId;
  private ZonedDateTime enrolmentTime;
  private Boolean archived;
  private Long participationTypeId;
  private Long enrolmentTypeId;
  private Boolean lodging;
  private CourseOptionality optionality;
  private Long billingDetailsId;
}
