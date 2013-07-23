package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseDescription.class, entityType = TranquilModelType.UPDATE)
public class CourseDescriptionUpdate extends CourseDescriptionComplete {

  public void setCourseBase(CourseBaseCompact courseBase) {
    super.setCourseBase(courseBase);
  }

  public CourseBaseCompact getCourseBase() {
    return (CourseBaseCompact)super.getCourseBase();
  }

  public void setCategory(CourseDescriptionCategoryCompact category) {
    super.setCategory(category);
  }

  public CourseDescriptionCategoryCompact getCategory() {
    return (CourseDescriptionCategoryCompact)super.getCategory();
  }

  public final static String[] properties = {"courseBase","category"};
}
