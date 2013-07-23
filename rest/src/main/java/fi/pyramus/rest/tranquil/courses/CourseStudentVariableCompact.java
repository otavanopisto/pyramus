package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseStudentVariable.class, entityType = TranquilModelType.COMPACT)
public class CourseStudentVariableCompact extends CourseStudentVariableBase {

  public Long getKey_id() {
    return key_id;
  }

  public void setKey_id(Long key_id) {
    this.key_id = key_id;
  }

  public Long getCourseStudent_id() {
    return courseStudent_id;
  }

  public void setCourseStudent_id(Long courseStudent_id) {
    this.courseStudent_id = courseStudent_id;
  }

  private Long key_id;

  private Long courseStudent_id;

  public final static String[] properties = {"key","courseStudent"};
}
