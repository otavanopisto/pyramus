package fi.pyramus.rest.model;

public class CourseLength {

  public CourseLength() {
    super();
  }

  public CourseLength(Double units, Long unitId) {
   this(null, units, unitId);
  }

  public CourseLength(Long id, Double units, Long unitId) {
    this();
    this.id = id;
    this.units = units;
    this.unitId = unitId;
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

  private Long id;
  private Double units;
  private Long unitId;
}
