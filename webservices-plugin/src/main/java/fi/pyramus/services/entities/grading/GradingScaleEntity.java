package fi.pyramus.services.entities.grading;

public class GradingScaleEntity {
  
  public GradingScaleEntity() {
  }

  public GradingScaleEntity(Long id, String name, String description, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private String description;
  private Boolean archived;

}
