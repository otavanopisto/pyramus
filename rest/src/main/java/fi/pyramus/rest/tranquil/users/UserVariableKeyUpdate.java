package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.users.UserVariableKey.class, entityType = TranquilModelType.UPDATE)
public class UserVariableKeyUpdate extends UserVariableKeyComplete {

  public final static String[] properties = {};
}
