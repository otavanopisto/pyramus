package fi.otavanopisto.pyramus.rest.model;

public class StudyProgramme {

  public StudyProgramme() {
  }

  public StudyProgramme(Long id, Long organizationId, String code, String name, Long categoryId, String officialEducationType, Boolean hasEvaluationFees, Boolean archived, String educationTypeCode) {
    super();
    this.id = id;
    this.organizationId = organizationId;
    this.code = code;
    this.name = name;
    this.categoryId = categoryId;
    this.officialEducationType = officialEducationType;
    this.hasEvaluationFees = hasEvaluationFees;
    this.archived = archived;
    this.educationTypeCode = educationTypeCode;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public Long getCategoryId() {
    return categoryId;
  }
  
  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }
  
  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  public Boolean getHasEvaluationFees() {
    return hasEvaluationFees;
  }

  public void setHasEvaluationFees(Boolean hasEvaluationFees) {
    this.hasEvaluationFees = hasEvaluationFees;
  }

  public String getOfficialEducationType() {
    return officialEducationType;
  }

  public void setOfficialEducationType(String officialEducationType) {
    this.officialEducationType = officialEducationType;
  }

  public String getEducationTypeCode() {
    return educationTypeCode;
  }

  public void setEducationTypeCode(String educationTypeCode) {
    this.educationTypeCode = educationTypeCode;
  }

  private Long id;
  private Long organizationId;
  private String name;
  private String code;
  private Long categoryId;
  private String officialEducationType;
  private Boolean hasEvaluationFees;
  private Boolean archived;
  private String educationTypeCode;
}