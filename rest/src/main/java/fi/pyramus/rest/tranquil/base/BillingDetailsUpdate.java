package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.BillingDetails.class, entityType = TranquilModelType.UPDATE)
public class BillingDetailsUpdate extends BillingDetailsComplete {

  public final static String[] properties = {};
}
