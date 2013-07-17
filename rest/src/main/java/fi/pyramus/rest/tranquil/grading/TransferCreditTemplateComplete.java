package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplate.class, entityType = TranquilModelType.COMPLETE)
public class TransferCreditTemplateComplete extends TransferCreditTemplateBase {

  public java.util.List<TranquilModelEntity> getCourses() {
    return courses;
  }

  public void setCourses(java.util.List<TranquilModelEntity> courses) {
    this.courses = courses;
  }

  private java.util.List<TranquilModelEntity> courses;

  public final static String[] properties = {"courses"};
}
