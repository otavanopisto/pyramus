package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.CourseBaseVariable.class, entityType = TranquilModelType.COMPACT)
public class CourseBaseVariableCompact extends CourseBaseVariableBase {

  public Long getCourseBase_id() {
    return courseBase_id;
  }

  public void setCourseBase_id(Long courseBase_id) {
    this.courseBase_id = courseBase_id;
  }

  public Long getKey_id() {
    return key_id;
  }

  public void setKey_id(Long key_id) {
    this.key_id = key_id;
  }

  private Long courseBase_id;

  private Long key_id;

  public final static String[] properties = {"courseBase","key"};
}
