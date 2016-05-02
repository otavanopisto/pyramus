package fi.pyramus.services.entities.base;

public class EducationTypeEntity {

  public EducationTypeEntity() {
  }
  
  public EducationTypeEntity(Long id, String name, String code, EducationSubtypeEntity[] subtypes, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.code = code;
    this.subtypes = subtypes;
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

  public EducationSubtypeEntity[] getSubtypes() {
    return subtypes;
  }

  public void setSubtypes(EducationSubtypeEntity[] subtypes) {
    this.subtypes = subtypes;
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
  private EducationSubtypeEntity subtypes[];
  private Boolean archived;
}
