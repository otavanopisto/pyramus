package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.BillingDetails.class, entityType = TranquilModelType.COMPLETE)
public class BillingDetailsComplete extends BillingDetailsBase {

  public final static String[] properties = {};
}
