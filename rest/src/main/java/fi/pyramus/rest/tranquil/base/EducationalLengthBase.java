package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.EducationalLength.class, entityType = TranquilModelType.BASE)
public class EducationalLengthBase implements fi.tranquil.TranquilModelEntity {

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

  public Double getBaseUnits() {
    return baseUnits;
  }

  public void setBaseUnits(Double baseUnits) {
    this.baseUnits = baseUnits;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private Double units;

  private Double baseUnits;

  private Long version;

  public final static String[] properties = {"id","units","baseUnits","version"};
}
