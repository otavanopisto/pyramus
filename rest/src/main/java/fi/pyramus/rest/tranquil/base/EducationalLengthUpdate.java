package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.EducationalLength.class, entityType = TranquilModelType.UPDATE)
public class EducationalLengthUpdate extends EducationalLengthComplete {

  public void setUnit(EducationalTimeUnitCompact unit) {
    super.setUnit(unit);
  }

  public EducationalTimeUnitCompact getUnit() {
    return (EducationalTimeUnitCompact)super.getUnit();
  }

  public final static String[] properties = {"unit"};
}
