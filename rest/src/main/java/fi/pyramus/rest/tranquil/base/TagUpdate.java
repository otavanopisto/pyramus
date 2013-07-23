package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Tag.class, entityType = TranquilModelType.UPDATE)
public class TagUpdate extends TagComplete {

  public final static String[] properties = {};
}
