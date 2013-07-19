package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.Grade.class, entityType = TranquilModelType.UPDATE)
public class GradeUpdate extends GradeComplete {

  public void setGradingScale(GradingScaleCompact gradingScale) {
    super.setGradingScale(gradingScale);
  }

  public GradingScaleCompact getGradingScale() {
    return (GradingScaleCompact)super.getGradingScale();
  }

  public final static String[] properties = {"gradingScale"};
}
