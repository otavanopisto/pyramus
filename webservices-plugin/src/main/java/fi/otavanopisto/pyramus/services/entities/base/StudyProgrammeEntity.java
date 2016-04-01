package fi.otavanopisto.pyramus.services.entities.base;

public class StudyProgrammeEntity {
  
  public StudyProgrammeEntity() {
  }

  public StudyProgrammeEntity(Long id, String name, String code, StudyProgrammeCategoryEntity category, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.code = code;
    this.category = category;
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

  public StudyProgrammeCategoryEntity getCategory() {
    return category;
  }

  public void setCategory(StudyProgrammeCategoryEntity category) {
    this.category = category;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private String code;
  private StudyProgrammeCategoryEntity category;
  private Boolean archived;
}
