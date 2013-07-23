package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.users.InternalAuth.class, entityType = TranquilModelType.COMPACT)
public class InternalAuthCompact extends InternalAuthBase {

  public final static String[] properties = {};
}
