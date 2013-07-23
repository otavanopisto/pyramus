package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Tag.class, entityType = TranquilModelType.COMPLETE)
public class TagComplete extends TagBase {

  public final static String[] properties = {};
}
