package fi.pyramus.rest.model;

public class EducationType {

  public EducationType() {
    super();
  }

  public EducationType(Long id, String name, String code, Boolean archived) {
    this();
    this.id = id;
    this.name = name;
    this.code = code;
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

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private String code;
  private Boolean archived;
}
