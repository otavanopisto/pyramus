package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.SchoolField.class, entityType = TranquilModelType.UPDATE)
public class SchoolFieldUpdate extends SchoolFieldComplete {

  public final static String[] properties = {};
}
