package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.Student.class, entityType = TranquilModelType.COMPACT)
public class StudentCompact extends StudentBase {

  public Long getPrimaryEmail_id() {
    return primaryEmail_id;
  }

  public void setPrimaryEmail_id(Long primaryEmail_id) {
    this.primaryEmail_id = primaryEmail_id;
  }

  public Long getAbstractStudent_id() {
    return abstractStudent_id;
  }

  public void setAbstractStudent_id(Long abstractStudent_id) {
    this.abstractStudent_id = abstractStudent_id;
  }

  public Long getMunicipality_id() {
    return municipality_id;
  }

  public void setMunicipality_id(Long municipality_id) {
    this.municipality_id = municipality_id;
  }

  public Long getNationality_id() {
    return nationality_id;
  }

  public void setNationality_id(Long nationality_id) {
    this.nationality_id = nationality_id;
  }

  public Long getLanguage_id() {
    return language_id;
  }

  public void setLanguage_id(Long language_id) {
    this.language_id = language_id;
  }

  public Long getSchool_id() {
    return school_id;
  }

  public void setSchool_id(Long school_id) {
    this.school_id = school_id;
  }

  public Long getStudyProgramme_id() {
    return studyProgramme_id;
  }

  public void setStudyProgramme_id(Long studyProgramme_id) {
    this.studyProgramme_id = studyProgramme_id;
  }

  public Long getStudyEndReason_id() {
    return studyEndReason_id;
  }

  public void setStudyEndReason_id(Long studyEndReason_id) {
    this.studyEndReason_id = studyEndReason_id;
  }

  public Long getActivityType_id() {
    return activityType_id;
  }

  public void setActivityType_id(Long activityType_id) {
    this.activityType_id = activityType_id;
  }

  public Long getEducationalLevel_id() {
    return educationalLevel_id;
  }

  public void setEducationalLevel_id(Long educationalLevel_id) {
    this.educationalLevel_id = educationalLevel_id;
  }

  public Long getExaminationType_id() {
    return examinationType_id;
  }

  public void setExaminationType_id(Long examinationType_id) {
    this.examinationType_id = examinationType_id;
  }

  public Long getDefaultAddress_id() {
    return defaultAddress_id;
  }

  public void setDefaultAddress_id(Long defaultAddress_id) {
    this.defaultAddress_id = defaultAddress_id;
  }

  public Long getDefaultEmail_id() {
    return defaultEmail_id;
  }

  public void setDefaultEmail_id(Long defaultEmail_id) {
    this.defaultEmail_id = defaultEmail_id;
  }

  public Long getDefaultPhone_id() {
    return defaultPhone_id;
  }

  public void setDefaultPhone_id(Long defaultPhone_id) {
    this.defaultPhone_id = defaultPhone_id;
  }

  public Long getContactInfo_id() {
    return contactInfo_id;
  }

  public void setContactInfo_id(Long contactInfo_id) {
    this.contactInfo_id = contactInfo_id;
  }

  public java.util.List<Long> getVariables_ids() {
    return variables_ids;
  }

  public void setVariables_ids(java.util.List<Long> variables_ids) {
    this.variables_ids = variables_ids;
  }

  public java.util.List<Long> getBillingDetails_ids() {
    return billingDetails_ids;
  }

  public void setBillingDetails_ids(java.util.List<Long> billingDetails_ids) {
    this.billingDetails_ids = billingDetails_ids;
  }

  private Long primaryEmail_id;

  private Long abstractStudent_id;

  private Long municipality_id;

  private Long nationality_id;

  private Long language_id;

  private Long school_id;

  private Long studyProgramme_id;

  private Long studyEndReason_id;

  private Long activityType_id;

  private Long educationalLevel_id;

  private Long examinationType_id;

  private Long defaultAddress_id;

  private Long defaultEmail_id;

  private Long defaultPhone_id;

  private Long contactInfo_id;

  private java.util.List<Long> variables_ids;

  private java.util.List<Long> billingDetails_ids;

  public final static String[] properties = {"primaryEmail","abstractStudent","municipality","nationality","language","school","studyProgramme","studyEndReason","activityType","educationalLevel","examinationType","defaultAddress","defaultEmail","defaultPhone","contactInfo","variables","billingDetails"};
}
