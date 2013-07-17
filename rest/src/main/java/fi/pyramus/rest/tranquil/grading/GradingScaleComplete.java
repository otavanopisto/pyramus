package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.GradingScale.class, entityType = TranquilModelType.COMPLETE)
public class GradingScaleComplete extends GradingScaleBase {

  public java.util.List<TranquilModelEntity> getGrades() {
    return grades;
  }

  public void setGrades(java.util.List<TranquilModelEntity> grades) {
    this.grades = grades;
  }

  private java.util.List<TranquilModelEntity> grades;

  public final static String[] properties = {"grades"};
}
