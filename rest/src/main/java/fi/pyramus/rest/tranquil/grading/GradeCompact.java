package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.Grade.class, entityType = TranquilModelType.COMPACT)
public class GradeCompact extends GradeBase {

  public Long getGradingScale_id() {
    return gradingScale_id;
  }

  public void setGradingScale_id(Long gradingScale_id) {
    this.gradingScale_id = gradingScale_id;
  }

  private Long gradingScale_id;

  public final static String[] properties = {"gradingScale"};
}
