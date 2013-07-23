package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.OtherCost.class, entityType = TranquilModelType.COMPLETE)
public class OtherCostComplete extends OtherCostBase {

  public TranquilModelEntity getCourse() {
    return course;
  }

  public void setCourse(TranquilModelEntity course) {
    this.course = course;
  }

  private TranquilModelEntity course;

  public final static String[] properties = {"course"};
}
