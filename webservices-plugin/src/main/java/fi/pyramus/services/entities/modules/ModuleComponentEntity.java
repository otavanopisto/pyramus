package fi.pyramus.services.entities.modules;

public class ModuleComponentEntity {
  
  public ModuleComponentEntity() {
  }

  public ModuleComponentEntity(Long id, String name, String description, Double length, Long lengthUnitId, Long moduleId) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.length = length;
    this.lengthUnitId = lengthUnitId;
    this.moduleId = moduleId;
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

  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    this.length = length;
  }

  public Long getLengthUnitId() {
    return lengthUnitId;
  }

  public void setLengthUnitId(Long lengthUnitId) {
    this.lengthUnitId = lengthUnitId;
  }

  public Long getModuleId() {
    return moduleId;
  }

  public void setModuleId(Long moduleId) {
    this.moduleId = moduleId;
  }

  private Long id;
  private String name;
  private String description;
  private Double length;
  private Long lengthUnitId;
  private Long moduleId;
}
