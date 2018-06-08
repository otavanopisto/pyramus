package fi.otavanopisto.pyramus.rest.model;

public class StudyProgramme {

  public StudyProgramme() {
  }

  public StudyProgramme(Long id, Long organizationId, String code, String name, Long categoryId, Boolean archived) {
    super();
    this.id = id;
    this.organizationId = organizationId;
    this.code = code;
    this.name = name;
    this.categoryId = categoryId;
    this.archived = archived;
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

  private Long id;
  private Long organizationId;
  private String name;
  private String code;
  private Long categoryId;
  private Boolean archived;
}