package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseDescription.class, entityType = TranquilModelType.COMPLETE)
public class CourseDescriptionComplete extends CourseDescriptionBase {

  public TranquilModelEntity getCourseBase() {
    return courseBase;
  }

  public void setCourseBase(TranquilModelEntity courseBase) {
    this.courseBase = courseBase;
  }

  public TranquilModelEntity getCategory() {
    return category;
  }

  public void setCategory(TranquilModelEntity category) {
    this.category = category;
  }

  private TranquilModelEntity courseBase;

  private TranquilModelEntity category;

  public final static String[] properties = {"courseBase","category"};
}
