package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.EducationalLength.class, entityType = TranquilModelType.COMPLETE)
public class EducationalLengthComplete extends EducationalLengthBase {

  public TranquilModelEntity getUnit() {
    return unit;
  }

  public void setUnit(TranquilModelEntity unit) {
    this.unit = unit;
  }

  private TranquilModelEntity unit;

  public final static String[] properties = {"unit"};
}
