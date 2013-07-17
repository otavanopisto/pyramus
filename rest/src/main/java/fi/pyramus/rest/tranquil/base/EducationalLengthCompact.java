package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.EducationalLength.class, entityType = TranquilModelType.COMPACT)
public class EducationalLengthCompact extends EducationalLengthBase {

  public Long getUnit_id() {
    return unit_id;
  }

  public void setUnit_id(Long unit_id) {
    this.unit_id = unit_id;
  }

  private Long unit_id;

  public final static String[] properties = {"unit"};
}
