package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.CreditVariableKey.class, entityType = TranquilModelType.COMPACT)
public class CreditVariableKeyCompact extends CreditVariableKeyBase {

  public final static String[] properties = {};
}
