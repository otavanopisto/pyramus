package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.GradingScale.class, entityType = TranquilModelType.UPDATE)
public class GradingScaleUpdate extends GradingScaleComplete {

  public void setGrades(java.util.List<GradeCompact> grades) {
    super.setGrades(grades);
  }

  public java.util.List<GradeCompact> getGrades() {
    return (java.util.List<GradeCompact>)super.getGrades();
  }

  public final static String[] properties = {"grades"};
}
