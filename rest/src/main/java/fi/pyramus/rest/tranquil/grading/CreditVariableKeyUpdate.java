package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CreditVariableKey.class, entityType = TranquilModelType.UPDATE)
public class CreditVariableKeyUpdate extends CreditVariableKeyComplete {

  public final static String[] properties = {};
}
