package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.TrackedEntityProperty.class, entityType = TranquilModelType.UPDATE)
public class TrackedEntityPropertyUpdate extends TrackedEntityPropertyComplete {

  public final static String[] properties = {};
}
