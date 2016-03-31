package fi.otavanopisto.pyramus.rest.model;

public class Nationality {

  public Nationality() {
  }

  public Nationality(Long id, String code, String name, Boolean archived) {
    super();
    this.id = id;
    this.code = code;
    this.name = name;
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

  private Long id;
  private String code;
  private String name;
  private Boolean archived;
}