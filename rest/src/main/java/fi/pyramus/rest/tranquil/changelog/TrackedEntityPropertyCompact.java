package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.TrackedEntityProperty.class, entityType = TranquilModelType.COMPACT)
public class TrackedEntityPropertyCompact extends TrackedEntityPropertyBase {

  public final static String[] properties = {};
}
