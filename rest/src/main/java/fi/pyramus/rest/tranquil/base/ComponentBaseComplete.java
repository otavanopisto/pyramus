package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ComponentBase.class, entityType = TranquilModelType.COMPLETE)
public class ComponentBaseComplete extends ComponentBaseBase {

  public TranquilModelEntity getLength() {
    return length;
  }

  public void setLength(TranquilModelEntity length) {
    this.length = length;
  }

  private TranquilModelEntity length;

  public final static String[] properties = {"length"};
}
