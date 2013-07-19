package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.users.InternalAuth.class, entityType = TranquilModelType.UPDATE)
public class InternalAuthUpdate extends InternalAuthComplete {

  public final static String[] properties = {};
}
