package fi.otavanopisto.pyramus.rest.model;

public class CourseLength {

  public CourseLength() {
  }
  
  public CourseLength(Long id, Double baseUnits, Double units, EducationalTimeUnit unit) {
    this.id = id;
    this.baseUnits = baseUnits;
    this.units = units;
    this.unit = unit;
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
  
  public Double getUnits() {
    return units;
  }
  
  public void setUnits(Double units) {
    this.units = units;
  }
  
  public EducationalTimeUnit getUnit() {
    return unit;
  }

  public void setUnit(EducationalTimeUnit unit) {
    this.unit = unit;
  }
  
  private Long id;
  private Double baseUnits;
  private Double units;
  private EducationalTimeUnit unit;
}
