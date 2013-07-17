package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.Student.class, entityType = TranquilModelType.COMPLETE)
public class StudentComplete extends StudentBase {

  public TranquilModelEntity getPrimaryEmail() {
    return primaryEmail;
  }

  public void setPrimaryEmail(TranquilModelEntity primaryEmail) {
    this.primaryEmail = primaryEmail;
  }

  public TranquilModelEntity getAbstractStudent() {
    return abstractStudent;
  }

  public void setAbstractStudent(TranquilModelEntity abstractStudent) {
    this.abstractStudent = abstractStudent;
  }

  public TranquilModelEntity getMunicipality() {
    return municipality;
  }

  public void setMunicipality(TranquilModelEntity municipality) {
    this.municipality = municipality;
  }

  public TranquilModelEntity getNationality() {
    return nationality;
  }

  public void setNationality(TranquilModelEntity nationality) {
    this.nationality = nationality;
  }

  public TranquilModelEntity getLanguage() {
    return language;
  }

  public void setLanguage(TranquilModelEntity language) {
    this.language = language;
  }

  public TranquilModelEntity getSchool() {
    return school;
  }

  public void setSchool(TranquilModelEntity school) {
    this.school = school;
  }

  public TranquilModelEntity getStudyProgramme() {
    return studyProgramme;
  }

  public void setStudyProgramme(TranquilModelEntity studyProgramme) {
    this.studyProgramme = studyProgramme;
  }

  public TranquilModelEntity getStudyEndReason() {
    return studyEndReason;
  }

  public void setStudyEndReason(TranquilModelEntity studyEndReason) {
    this.studyEndReason = studyEndReason;
  }

  public TranquilModelEntity getActivityType() {
    return activityType;
  }

  public void setActivityType(TranquilModelEntity activityType) {
    this.activityType = activityType;
  }

  public TranquilModelEntity getEducationalLevel() {
    return educationalLevel;
  }

  public void setEducationalLevel(TranquilModelEntity educationalLevel) {
    this.educationalLevel = educationalLevel;
  }

  public TranquilModelEntity getExaminationType() {
    return examinationType;
  }

  public void setExaminationType(TranquilModelEntity examinationType) {
    this.examinationType = examinationType;
  }

  public TranquilModelEntity getDefaultAddress() {
    return defaultAddress;
  }

  public void setDefaultAddress(TranquilModelEntity defaultAddress) {
    this.defaultAddress = defaultAddress;
  }

  public TranquilModelEntity getDefaultEmail() {
    return defaultEmail;
  }

  public void setDefaultEmail(TranquilModelEntity defaultEmail) {
    this.defaultEmail = defaultEmail;
  }

  public TranquilModelEntity getDefaultPhone() {
    return defaultPhone;
  }

  public void setDefaultPhone(TranquilModelEntity defaultPhone) {
    this.defaultPhone = defaultPhone;
  }

  public TranquilModelEntity getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(TranquilModelEntity contactInfo) {
    this.contactInfo = contactInfo;
  }

  public java.util.List<TranquilModelEntity> getVariables() {
    return variables;
  }

  public void setVariables(java.util.List<TranquilModelEntity> variables) {
    this.variables = variables;
  }

  public java.util.List<TranquilModelEntity> getBillingDetails() {
    return billingDetails;
  }

  public void setBillingDetails(java.util.List<TranquilModelEntity> billingDetails) {
    this.billingDetails = billingDetails;
  }

  private TranquilModelEntity primaryEmail;

  private TranquilModelEntity abstractStudent;

  private TranquilModelEntity municipality;

  private TranquilModelEntity nationality;

  private TranquilModelEntity language;

  private TranquilModelEntity school;

  private TranquilModelEntity studyProgramme;

  private TranquilModelEntity studyEndReason;

  private TranquilModelEntity activityType;

  private TranquilModelEntity educationalLevel;

  private TranquilModelEntity examinationType;

  private TranquilModelEntity defaultAddress;

  private TranquilModelEntity defaultEmail;

  private TranquilModelEntity defaultPhone;

  private TranquilModelEntity contactInfo;

  private java.util.List<TranquilModelEntity> variables;

  private java.util.List<TranquilModelEntity> billingDetails;

  public final static String[] properties = {"primaryEmail","abstractStudent","municipality","nationality","language","school","studyProgramme","studyEndReason","activityType","educationalLevel","examinationType","defaultAddress","defaultEmail","defaultPhone","contactInfo","variables","billingDetails"};
}
