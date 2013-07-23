package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.ContactURLType.class, entityType = TranquilModelType.COMPACT)
public class ContactURLTypeCompact extends ContactURLTypeBase {

  public final static String[] properties = {};
}
