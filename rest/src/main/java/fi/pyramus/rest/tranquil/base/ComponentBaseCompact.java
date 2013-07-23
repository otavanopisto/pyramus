package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.ComponentBase.class, entityType = TranquilModelType.COMPACT)
public class ComponentBaseCompact extends ComponentBaseBase {

  public Long getLength_id() {
    return length_id;
  }

  public void setLength_id(Long length_id) {
    this.length_id = length_id;
  }

  private Long length_id;

  public final static String[] properties = {"length"};
}
