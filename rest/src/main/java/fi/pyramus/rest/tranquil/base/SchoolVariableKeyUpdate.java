package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.SchoolVariableKey.class, entityType = TranquilModelType.UPDATE)
public class SchoolVariableKeyUpdate extends SchoolVariableKeyComplete {

  public final static String[] properties = {};
}
