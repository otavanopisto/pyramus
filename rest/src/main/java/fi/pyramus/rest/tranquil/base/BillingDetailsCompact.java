package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.BillingDetails.class, entityType = TranquilModelType.COMPACT)
public class BillingDetailsCompact extends BillingDetailsBase {

  public final static String[] properties = {};
}
