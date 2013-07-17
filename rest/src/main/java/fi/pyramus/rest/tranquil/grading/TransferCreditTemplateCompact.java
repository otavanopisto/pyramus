package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplate.class, entityType = TranquilModelType.COMPACT)
public class TransferCreditTemplateCompact extends TransferCreditTemplateBase {

  public java.util.List<Long> getCourses_ids() {
    return courses_ids;
  }

  public void setCourses_ids(java.util.List<Long> courses_ids) {
    this.courses_ids = courses_ids;
  }

  private java.util.List<Long> courses_ids;

  public final static String[] properties = {"courses"};
}
