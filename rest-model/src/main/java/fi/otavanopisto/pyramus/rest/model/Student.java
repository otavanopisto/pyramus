package fi.otavanopisto.pyramus.rest.model;

import java.util.List;
import java.util.Map;

import java.time.OffsetDateTime;
import fi.otavanopisto.security.ContextReference;

public class Student implements ContextReference {

  public Student() {
    super();
  }
  
  public Student(Long id, Long personId, String firstName, String lastName, String nickname, String additionalInfo, String additionalContactInfo, Long nationalityId,
      Long languageId, Long municipalityId, Long schoolId, Long activityTypeId, Long examinationTypeId, Long educationalLevelId, OffsetDateTime studyTimeEnd,
      Long studyProgrammeId, Long curriculumId, Double previousStudies, String education, Boolean lodging, OffsetDateTime studyStartDate, OffsetDateTime studyEndDate, Long studyEndReasonId,
      String studyEndText, Map<String, String> variables, List<String> tags, Boolean archived, MatriculationEligibilities matriculationEligibility) {
    super();
    this.id = id;
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.nickname = nickname;
    this.additionalInfo = additionalInfo;
    this.additionalContactInfo = additionalContactInfo;
    this.nationalityId = nationalityId;
    this.languageId = languageId;
    this.municipalityId = municipalityId;
    this.schoolId = schoolId;
    this.activityTypeId = activityTypeId;
    this.examinationTypeId = examinationTypeId;
    this.educationalLevelId = educationalLevelId;
    this.studyTimeEnd = studyTimeEnd;
    this.studyProgrammeId = studyProgrammeId;
    this.setCurriculumId(curriculumId);
    this.previousStudies = previousStudies;
    this.education = education;
    this.lodging = lodging;
    this.studyStartDate = studyStartDate;
    this.studyEndDate = studyEndDate;
    this.studyEndReasonId = studyEndReasonId;
    this.studyEndText = studyEndText;
    this.variables = variables;
    this.tags = tags;
    this.archived = archived;
    this.matriculationEligibility = matriculationEligibility;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
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

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }
  
  public String getAdditionalContactInfo() {
    return additionalContactInfo;
  }
  
  public void setAdditionalContactInfo(String additionalContactInfo) {
    this.additionalContactInfo = additionalContactInfo;
  }

  public Long getNationalityId() {
    return nationalityId;
  }

  public void setNationalityId(Long nationalityId) {
    this.nationalityId = nationalityId;
  }

  public Long getLanguageId() {
    return languageId;
  }

  public void setLanguageId(Long languageId) {
    this.languageId = languageId;
  }

  public Long getMunicipalityId() {
    return municipalityId;
  }

  public void setMunicipalityId(Long municipalityId) {
    this.municipalityId = municipalityId;
  }

  public Long getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(Long schoolId) {
    this.schoolId = schoolId;
  }

  public Long getActivityTypeId() {
    return activityTypeId;
  }

  public void setActivityTypeId(Long activityTypeId) {
    this.activityTypeId = activityTypeId;
  }

  public Long getExaminationTypeId() {
    return examinationTypeId;
  }

  public void setExaminationTypeId(Long examinationTypeId) {
    this.examinationTypeId = examinationTypeId;
  }

  public Long getEducationalLevelId() {
    return educationalLevelId;
  }

  public void setEducationalLevelId(Long educationalLevelId) {
    this.educationalLevelId = educationalLevelId;
  }

  public OffsetDateTime getStudyTimeEnd() {
    return studyTimeEnd;
  }

  public void setStudyTimeEnd(OffsetDateTime studyTimeEnd) {
    this.studyTimeEnd = studyTimeEnd;
  }

  public Long getStudyProgrammeId() {
    return studyProgrammeId;
  }

  public void setStudyProgrammeId(Long studyProgrammeId) {
    this.studyProgrammeId = studyProgrammeId;
  }

  public Double getPreviousStudies() {
    return previousStudies;
  }

  public void setPreviousStudies(Double previousStudies) {
    this.previousStudies = previousStudies;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public Boolean getLodging() {
    return lodging;
  }

  public void setLodging(Boolean lodging) {
    this.lodging = lodging;
  }

  public OffsetDateTime getStudyStartDate() {
    return studyStartDate;
  }

  public void setStudyStartDate(OffsetDateTime studyStartDate) {
    this.studyStartDate = studyStartDate;
  }

  public OffsetDateTime getStudyEndDate() {
    return studyEndDate;
  }

  public void setStudyEndDate(OffsetDateTime studyEndDate) {
    this.studyEndDate = studyEndDate;
  }

  public Long getStudyEndReasonId() {
    return studyEndReasonId;
  }

  public void setStudyEndReasonId(Long studyEndReasonId) {
    this.studyEndReasonId = studyEndReasonId;
  }

  public String getStudyEndText() {
    return studyEndText;
  }

  public void setStudyEndText(String studyEndText) {
    this.studyEndText = studyEndText;
  }

  public Map<String, String> getVariables() {
    return variables;
  }

  public void setVariables(Map<String, String> variables) {
    this.variables = variables;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getCurriculumId() {
    return curriculumId;
  }

  public void setCurriculumId(Long curriculumId) {
    this.curriculumId = curriculumId;
  }

  public MatriculationEligibilities getMatriculationEligibility() {
    return matriculationEligibility;
  }

  public void setMatriculationEligibility(MatriculationEligibilities matriculationEligibility) {
    this.matriculationEligibility = matriculationEligibility;
  }

  private Long id;
  private Long personId;
  private String firstName;
  private String lastName;
  private String nickname;
  private String additionalInfo;
  private String additionalContactInfo;
  private Long nationalityId;
  private Long languageId;
  private Long municipalityId;
  private Long schoolId;
  private Long activityTypeId;
  private Long examinationTypeId;
  private Long educationalLevelId;
  private OffsetDateTime studyTimeEnd;
  private Long studyProgrammeId;
  private Long curriculumId;
  private Double previousStudies;
  private String education;
  private Boolean lodging;
  private OffsetDateTime studyStartDate;
  private OffsetDateTime studyEndDate;
  private Long studyEndReasonId;
  private String studyEndText;
  private Map<String, String> variables;
  private List<String> tags;
  private Boolean archived;
  private MatriculationEligibilities matriculationEligibility;
}
