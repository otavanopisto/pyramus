package fi.pyramus.services.entities.base;

public class EducationalTimeUnitEntity {
  
  public EducationalTimeUnitEntity() {
  }

  public EducationalTimeUnitEntity(Long id, Double baseUnits, String name, Boolean archived) {
    super();
    this.id = id;
    this.baseUnits = baseUnits;
    this.name = name;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getBaseUnits() {
    return baseUnits;
  }

  public void setBaseUnits(Double baseUnits) {
    this.baseUnits = baseUnits;
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
  private Double baseUnits;
  private String name;
  private Boolean archived;
}
