package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.TransferCredit.class, entityType = TranquilModelType.BASE)
public class TransferCreditBase implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private java.util.Date date;

  private String verbalAssessment;

  private Boolean archived;

  private fi.pyramus.domainmodel.grading.CreditType creditType;

  private Long version;

  private String courseName;

  private Integer courseNumber;

  private fi.pyramus.domainmodel.base.CourseOptionality optionality;

  public final static String[] properties = {"id","date","verbalAssessment","archived","creditType","version","courseName","courseNumber","optionality"};
}
