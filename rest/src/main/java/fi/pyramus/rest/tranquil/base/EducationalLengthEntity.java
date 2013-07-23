package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.EducationalLength.class, entityType = TranquilModelType.COMPACT)
public class EducationalLengthEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getUnit_id() {
    return unit_id;
  }

  public void setUnit_id(Long unit_id) {
    this.unit_id = unit_id;
  }

  private Long id;

  private Double units;

  private Long version;

  private Long unit_id;

  public final static String[] properties = {"id","units","version","unit"};
}
