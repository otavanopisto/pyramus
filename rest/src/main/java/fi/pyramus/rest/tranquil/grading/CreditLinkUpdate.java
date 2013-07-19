package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CreditLink.class, entityType = TranquilModelType.UPDATE)
public class CreditLinkUpdate extends CreditLinkComplete {

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public void setCredit(CreditCompact credit) {
    super.setCredit(credit);
  }

  public CreditCompact getCredit() {
    return (CreditCompact)super.getCredit();
  }

  public void setCreator(UserCompact creator) {
    super.setCreator(creator);
  }

  public UserCompact getCreator() {
    return (UserCompact)super.getCreator();
  }

  public final static String[] properties = {"student","credit","creator"};
}
