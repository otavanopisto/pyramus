package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.GradingScale.class, entityType = TranquilModelType.COMPACT)
public class GradingScaleCompact extends GradingScaleBase {

  public java.util.List<Long> getGrades_ids() {
    return grades_ids;
  }

  public void setGrades_ids(java.util.List<Long> grades_ids) {
    this.grades_ids = grades_ids;
  }

  private java.util.List<Long> grades_ids;

  public final static String[] properties = {"grades"};
}
