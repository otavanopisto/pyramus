package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.Grade.class, entityType = TranquilModelType.COMPLETE)
public class GradeComplete extends GradeBase {

  public TranquilModelEntity getGradingScale() {
    return gradingScale;
  }

  public void setGradingScale(TranquilModelEntity gradingScale) {
    this.gradingScale = gradingScale;
  }

  private TranquilModelEntity gradingScale;

  public final static String[] properties = {"gradingScale"};
}
