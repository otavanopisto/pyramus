package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.MagicKey.class, entityType = TranquilModelType.COMPACT)
public class MagicKeyCompact extends MagicKeyBase {

  public final static String[] properties = {};
}
