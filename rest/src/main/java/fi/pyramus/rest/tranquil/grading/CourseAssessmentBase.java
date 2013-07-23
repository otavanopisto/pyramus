package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CourseAssessment.class, entityType = TranquilModelType.BASE)
public class CourseAssessmentBase implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private java.util.Date date;

  private String verbalAssessment;

  private Boolean archived;

  private fi.pyramus.domainmodel.grading.CreditType creditType;

  private Long version;

  public final static String[] properties = {"id","date","verbalAssessment","archived","creditType","version"};
}
