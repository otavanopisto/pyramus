package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseStudent.class, entityType = TranquilModelType.COMPACT)
public class CourseStudentEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.util.Date getEnrolmentTime() {
    return enrolmentTime;
  }

  public void setEnrolmentTime(java.util.Date enrolmentTime) {
    this.enrolmentTime = enrolmentTime;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getLodging() {
    return lodging;
  }

  public void setLodging(Boolean lodging) {
    this.lodging = lodging;
  }

  public fi.pyramus.domainmodel.base.CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(fi.pyramus.domainmodel.base.CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(Long course_id) {
    this.course_id = course_id;
  }

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  public Long getParticipationType_id() {
    return participationType_id;
  }

  public void setParticipationType_id(Long participationType_id) {
    this.participationType_id = participationType_id;
  }

  public Long getCourseEnrolmentType_id() {
    return courseEnrolmentType_id;
  }

  public void setCourseEnrolmentType_id(Long courseEnrolmentType_id) {
    this.courseEnrolmentType_id = courseEnrolmentType_id;
  }

  public Long getBillingDetails_id() {
    return billingDetails_id;
  }

  public void setBillingDetails_id(Long billingDetails_id) {
    this.billingDetails_id = billingDetails_id;
  }

  private Long id;

  private java.util.Date enrolmentTime;

  private Boolean archived;

  private Boolean lodging;

  private fi.pyramus.domainmodel.base.CourseOptionality optionality;

  private Long version;

  private Long course_id;

  private Long student_id;

  private Long participationType_id;

  private Long courseEnrolmentType_id;

  private Long billingDetails_id;

  public final static String[] properties = {"id","enrolmentTime","archived","lodging","optionality","version","course","student","participationType","courseEnrolmentType","billingDetails"};
}
