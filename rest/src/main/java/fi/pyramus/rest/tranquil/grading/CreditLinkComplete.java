package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CreditLink.class, entityType = TranquilModelType.COMPLETE)
public class CreditLinkComplete extends CreditLinkBase {

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  public TranquilModelEntity getCredit() {
    return credit;
  }

  public void setCredit(TranquilModelEntity credit) {
    this.credit = credit;
  }

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  private TranquilModelEntity student;

  private TranquilModelEntity credit;

  private TranquilModelEntity creator;

  public final static String[] properties = {"student","credit","creator"};
}
