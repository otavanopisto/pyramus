package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.Student.class, entityType = TranquilModelType.COMPACT)
public class StudentEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public java.util.Date getStudyTimeEnd() {
    return studyTimeEnd;
  }

  public void setStudyTimeEnd(java.util.Date studyTimeEnd) {
    this.studyTimeEnd = studyTimeEnd;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public java.util.Date getStudyEndDate() {
    return studyEndDate;
  }

  public void setStudyEndDate(java.util.Date studyEndDate) {
    this.studyEndDate = studyEndDate;
  }

  public java.util.Date getStudyStartDate() {
    return studyStartDate;
  }

  public void setStudyStartDate(java.util.Date studyStartDate) {
    this.studyStartDate = studyStartDate;
  }

  public Double getPreviousStudies() {
    return previousStudies;
  }

  public void setPreviousStudies(Double previousStudies) {
    this.previousStudies = previousStudies;
  }

  public String getStudyEndText() {
    return studyEndText;
  }

  public void setStudyEndText(String studyEndText) {
    this.studyEndText = studyEndText;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Boolean getLodging() {
    return lodging;
  }

  public void setLodging(Boolean lodging) {
    this.lodging = lodging;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  public java.util.List<Long> getBillingDetails_ids() {
    return billingDetails_ids;
  }

  public void setBillingDetails_ids(java.util.List<Long> billingDetails_ids) {
    this.billingDetails_ids = billingDetails_ids;
  }

  private Long id;

  private String firstName;

  private String lastName;

  private String additionalInfo;

  private java.util.Date studyTimeEnd;

  private Boolean archived;

  private java.util.Date studyEndDate;

  private java.util.Date studyStartDate;

  private Double previousStudies;

  private String studyEndText;

  private String education;

  private String nickname;

  private Boolean lodging;

  private Long version;

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

  private Long contactInfo_id;

  private java.util.List<Long> variables_ids;

  private java.util.List<Long> tags_ids;

  private java.util.List<Long> billingDetails_ids;

  public final static String[] properties = {"id","firstName","lastName","additionalInfo","studyTimeEnd","archived","studyEndDate","studyStartDate","previousStudies","studyEndText","education","nickname","lodging","version","abstractStudent","municipality","nationality","language","school","studyProgramme","studyEndReason","activityType","educationalLevel","examinationType","contactInfo","variables","tags","billingDetails"};
}
