package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.CreditVariable.class, entityType = TranquilModelType.COMPACT)
public class CreditVariableCompact extends CreditVariableBase {

  public Long getCredit_id() {
    return credit_id;
  }

  public void setCredit_id(Long credit_id) {
    this.credit_id = credit_id;
  }

  public Long getKey_id() {
    return key_id;
  }

  public void setKey_id(Long key_id) {
    this.key_id = key_id;
  }

  private Long credit_id;

  private Long key_id;

  public final static String[] properties = {"credit","key"};
}
