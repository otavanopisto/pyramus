package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.CreditLink.class, entityType = TranquilModelType.COMPACT)
public class CreditLinkCompact extends CreditLinkBase {

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  public Long getCredit_id() {
    return credit_id;
  }

  public void setCredit_id(Long credit_id) {
    this.credit_id = credit_id;
  }

  public Long getCreator_id() {
    return creator_id;
  }

  public void setCreator_id(Long creator_id) {
    this.creator_id = creator_id;
  }

  private Long student_id;

  private Long credit_id;

  private Long creator_id;

  public final static String[] properties = {"student","credit","creator"};
}
