package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.Credit.class, entityType = TranquilModelType.COMPACT)
public class CreditEntity implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private java.util.Date date;

  private String verbalAssessment;

  private Boolean archived;

  private fi.pyramus.domainmodel.grading.CreditType creditType;

  private Long version;

  private Long grade_id;

  private Long assessingUser_id;

  public final static String[] properties = {"id","date","verbalAssessment","archived","creditType","version","grade","assessingUser"};
}
