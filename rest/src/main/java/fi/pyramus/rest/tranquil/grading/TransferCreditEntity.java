package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.TransferCredit.class, entityType = TranquilModelType.COMPACT)
public class TransferCreditEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

  public String getVerbalAssessment() {
    return verbalAssessment;
  }

  public void setVerbalAssessment(String verbalAssessment) {
    this.verbalAssessment = verbalAssessment;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public fi.pyramus.domainmodel.grading.CreditType getCreditType() {
    return creditType;
  }

  public void setCreditType(fi.pyramus.domainmodel.grading.CreditType creditType) {
    this.creditType = creditType;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public fi.pyramus.domainmodel.base.CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(fi.pyramus.domainmodel.base.CourseOptionality optionality) {
    this.optionality = optionality;
  }

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

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  private Long id;

  private java.util.Date date;

  private String verbalAssessment;

  private Boolean archived;

  private fi.pyramus.domainmodel.grading.CreditType creditType;

  private Long version;

  private String courseName;

  private Integer courseNumber;

  private fi.pyramus.domainmodel.base.CourseOptionality optionality;

  private Long grade_id;

  private Long assessingUser_id;

  private Long courseLength_id;

  private Long school_id;

  private Long subject_id;

  private Long student_id;

  public final static String[] properties = {"id","date","verbalAssessment","archived","creditType","version","courseName","courseNumber","optionality","grade","assessingUser","courseLength","school","subject","student"};
}
