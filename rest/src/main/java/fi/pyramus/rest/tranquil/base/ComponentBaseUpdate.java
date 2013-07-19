package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ComponentBase.class, entityType = TranquilModelType.UPDATE)
public class ComponentBaseUpdate extends ComponentBaseComplete {

  public void setLength(EducationalLengthCompact length) {
    super.setLength(length);
  }

  public EducationalLengthCompact getLength() {
    return (EducationalLengthCompact)super.getLength();
  }

  public final static String[] properties = {"length"};
}
