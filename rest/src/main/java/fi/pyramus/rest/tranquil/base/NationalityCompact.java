package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Nationality.class, entityType = TranquilModelType.COMPACT)
public class NationalityCompact extends NationalityBase {

  public final static String[] properties = {};
}
