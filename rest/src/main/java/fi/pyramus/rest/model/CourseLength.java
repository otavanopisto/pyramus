package fi.pyramus.rest.model;

public class CourseLength {

  public CourseLength() {
  }

  public CourseLength(Long id, Double units, Long unitId, Long version) {
    super();
    this.id = id;
    this.units = units;
    this.unitId = unitId;
    this.version = version;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getUnits() {
    return units;
  }

  public void setUnits(Double units) {
    this.units = units;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;
  private Double units;
  private Long unitId;
  private Long version;
}
