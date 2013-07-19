package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CreditVariable.class, entityType = TranquilModelType.UPDATE)
public class CreditVariableUpdate extends CreditVariableComplete {

  public void setCredit(CreditCompact credit) {
    super.setCredit(credit);
  }

  public CreditCompact getCredit() {
    return (CreditCompact)super.getCredit();
  }

  public void setKey(CreditVariableKeyCompact key) {
    super.setKey(key);
  }

  public CreditVariableKeyCompact getKey() {
    return (CreditVariableKeyCompact)super.getKey();
  }

  public final static String[] properties = {"credit","key"};
}
