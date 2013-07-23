package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.CourseBaseVariable.class, entityType = TranquilModelType.UPDATE)
public class CourseBaseVariableUpdate extends CourseBaseVariableComplete {

  public void setCourseBase(CourseBaseCompact courseBase) {
    super.setCourseBase(courseBase);
  }

  public CourseBaseCompact getCourseBase() {
    return (CourseBaseCompact)super.getCourseBase();
  }

  public void setKey(CourseBaseVariableKeyCompact key) {
    super.setKey(key);
  }

  public CourseBaseVariableKeyCompact getKey() {
    return (CourseBaseVariableKeyCompact)super.getKey();
  }

  public final static String[] properties = {"courseBase","key"};
}
