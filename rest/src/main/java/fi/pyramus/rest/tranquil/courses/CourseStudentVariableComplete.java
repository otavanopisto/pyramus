package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseStudentVariable.class, entityType = TranquilModelType.COMPLETE)
public class CourseStudentVariableComplete extends CourseStudentVariableBase {

  public TranquilModelEntity getKey() {
    return key;
  }

  public void setKey(TranquilModelEntity key) {
    this.key = key;
  }

  public TranquilModelEntity getCourseStudent() {
    return courseStudent;
  }

  public void setCourseStudent(TranquilModelEntity courseStudent) {
    this.courseStudent = courseStudent;
  }

  private TranquilModelEntity key;

  private TranquilModelEntity courseStudent;

  public final static String[] properties = {"key","courseStudent"};
}
