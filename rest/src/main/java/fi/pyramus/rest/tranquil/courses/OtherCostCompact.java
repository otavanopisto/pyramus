package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.OtherCost.class, entityType = TranquilModelType.COMPACT)
public class OtherCostCompact extends OtherCostBase {

  public Long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(Long course_id) {
    this.course_id = course_id;
  }

  private Long course_id;

  public final static String[] properties = {"course"};
}
