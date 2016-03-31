package fi.otavanopisto.pyramus.rest.model;

public class ContactType {

  public ContactType() {
  }

  public ContactType(Long id, String name, Boolean archived, Boolean nonUnique) {
    super();
    this.id = id;
    this.name = name;
    this.archived = archived;
    this.nonUnique = nonUnique;
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

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getNonUnique() {
    return nonUnique;
  }

  public void setNonUnique(Boolean nonUnique) {
    this.nonUnique = nonUnique;
  }

  private Long id;
  private String name;
  private Boolean nonUnique;
  private Boolean archived;
}
