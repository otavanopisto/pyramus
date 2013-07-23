package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.EducationType.class, entityType = TranquilModelType.COMPLETE)
public class EducationTypeComplete extends EducationTypeBase {

  public java.util.List<TranquilModelEntity> getSubtypes() {
    return subtypes;
  }

  public void setSubtypes(java.util.List<TranquilModelEntity> subtypes) {
    this.subtypes = subtypes;
  }

  private java.util.List<TranquilModelEntity> subtypes;

  public final static String[] properties = {"subtypes"};
}
