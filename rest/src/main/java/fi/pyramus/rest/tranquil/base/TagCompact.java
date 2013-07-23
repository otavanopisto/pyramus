package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Tag.class, entityType = TranquilModelType.COMPACT)
public class TagCompact extends TagBase {

  public final static String[] properties = {};
}
