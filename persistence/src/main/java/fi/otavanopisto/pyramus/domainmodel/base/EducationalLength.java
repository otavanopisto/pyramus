package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;


@Entity
public class EducationalLength {
  
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

  public EducationalTimeUnit getUnit() {
    return unit;
  }

  public void setUnit(EducationalTimeUnit unit) {
    this.unit = unit;
  }

  @Transient
  public Double getBaseUnits() {
    return getUnits() * getUnit().getBaseUnits();
  }
  
  @Transient
  public void setBaseUnits(Double baseUnits) {
    setUnits(baseUnits / getUnit().getBaseUnits());
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="EducationalLength")  
  @TableGenerator(name="EducationalLength", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  private Double units;
  
  @ManyToOne 
  @JoinColumn (name = "unit")
  private EducationalTimeUnit unit;

  @Version
  @Column(nullable = false)
  private Long version;
}
