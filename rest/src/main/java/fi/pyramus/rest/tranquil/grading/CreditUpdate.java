package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.Credit.class, entityType = TranquilModelType.UPDATE)
public class CreditUpdate extends CreditComplete {

  public void setGrade(GradeCompact grade) {
    super.setGrade(grade);
  }

  public GradeCompact getGrade() {
    return (GradeCompact)super.getGrade();
  }

  public void setAssessingUser(UserCompact assessingUser) {
    super.setAssessingUser(assessingUser);
  }

  public UserCompact getAssessingUser() {
    return (UserCompact)super.getAssessingUser();
  }

  public final static String[] properties = {"grade","assessingUser"};
}
