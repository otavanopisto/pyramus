package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CreditVariable.class, entityType = TranquilModelType.COMPLETE)
public class CreditVariableComplete extends CreditVariableBase {

  public TranquilModelEntity getCredit() {
    return credit;
  }

  public void setCredit(TranquilModelEntity credit) {
    this.credit = credit;
  }

  public TranquilModelEntity getKey() {
    return key;
  }

  public void setKey(TranquilModelEntity key) {
    this.key = key;
  }

  private TranquilModelEntity credit;

  private TranquilModelEntity key;

  public final static String[] properties = {"credit","key"};
}
