package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.CourseBaseVariable.class, entityType = TranquilModelType.COMPLETE)
public class CourseBaseVariableComplete extends CourseBaseVariableBase {

  public TranquilModelEntity getCourseBase() {
    return courseBase;
  }

  public void setCourseBase(TranquilModelEntity courseBase) {
    this.courseBase = courseBase;
  }

  public TranquilModelEntity getKey() {
    return key;
  }

  public void setKey(TranquilModelEntity key) {
    this.key = key;
  }

  private TranquilModelEntity courseBase;

  private TranquilModelEntity key;

  public final static String[] properties = {"courseBase","key"};
}
