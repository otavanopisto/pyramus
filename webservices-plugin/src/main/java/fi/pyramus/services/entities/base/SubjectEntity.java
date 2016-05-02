package fi.pyramus.services.entities.base;

public class SubjectEntity {

  public SubjectEntity() {
  }
  
  public SubjectEntity(Long id, String code, String name, EducationTypeEntity educationType, Boolean archived) {
    super();
    this.id = id;
    this.code = code;
    this.name = name;
    this.educationType = educationType;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public EducationTypeEntity getEducationType() {
    return educationType;
  }

  public void setEducationType(EducationTypeEntity educationType) {
    this.educationType = educationType;
  }

  private Long id;
  private String code;
  private String name;
  private Boolean archived;
  private EducationTypeEntity educationType;
}
