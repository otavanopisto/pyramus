package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.Student.class, entityType = TranquilModelType.BASE)
public class StudentBase implements fi.tranquil.TranquilModelEntity {

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

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
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

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean getHasStartedStudies() {
    return hasStartedStudies;
  }

  public void setHasStartedStudies(boolean hasStartedStudies) {
    this.hasStartedStudies = hasStartedStudies;
  }

  public boolean getHasFinishedStudies() {
    return hasFinishedStudies;
  }

  public void setHasFinishedStudies(boolean hasFinishedStudies) {
    this.hasFinishedStudies = hasFinishedStudies;
  }

  public java.util.Map<java.lang.String,java.lang.String> getVariablesAsStringMap() {
    return variablesAsStringMap;
  }

  public void setVariablesAsStringMap(java.util.Map<java.lang.String,java.lang.String> variablesAsStringMap) {
    this.variablesAsStringMap = variablesAsStringMap;
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

  public java.util.Set<fi.pyramus.domainmodel.base.Tag> getTags() {
    return tags;
  }

  public void setTags(java.util.Set<fi.pyramus.domainmodel.base.Tag> tags) {
    this.tags = tags;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String firstName;

  private String lastName;

  private String fullName;

  private String additionalInfo;

  private java.util.Date studyTimeEnd;

  private Boolean archived;

  private java.util.Date studyEndDate;

  private java.util.Date studyStartDate;

  private Double previousStudies;

  private String studyEndText;

  private boolean active;

  private boolean hasStartedStudies;

  private boolean hasFinishedStudies;

  private java.util.Map<java.lang.String,java.lang.String> variablesAsStringMap;

  private String education;

  private String nickname;

  private Boolean lodging;

  private java.util.Set<fi.pyramus.domainmodel.base.Tag> tags;

  private Long version;

  public final static String[] properties = {"id","firstName","lastName","fullName","additionalInfo","studyTimeEnd","archived","studyEndDate","studyStartDate","previousStudies","studyEndText","active","hasStartedStudies","hasFinishedStudies","variablesAsStringMap","education","nickname","lodging","tags","version"};
}
