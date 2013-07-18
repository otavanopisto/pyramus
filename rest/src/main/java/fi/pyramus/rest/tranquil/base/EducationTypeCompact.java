package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.EducationType.class, entityType = TranquilModelType.COMPACT)
public class EducationTypeCompact extends EducationTypeBase {

  public java.util.List<Long> getSubtypes_ids() {
    return subtypes_ids;
  }

  public void setSubtypes_ids(java.util.List<Long> subtypes_ids) {
    this.subtypes_ids = subtypes_ids;
  }

  private java.util.List<Long> subtypes_ids;

  public final static String[] properties = {"subtypes"};
}
