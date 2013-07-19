package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseStudentVariable.class, entityType = TranquilModelType.UPDATE)
public class CourseStudentVariableUpdate extends CourseStudentVariableComplete {

  public void setKey(CourseStudentVariableKeyCompact key) {
    super.setKey(key);
  }

  public CourseStudentVariableKeyCompact getKey() {
    return (CourseStudentVariableKeyCompact)super.getKey();
  }

  public void setCourseStudent(CourseStudentCompact courseStudent) {
    super.setCourseStudent(courseStudent);
  }

  public CourseStudentCompact getCourseStudent() {
    return (CourseStudentCompact)super.getCourseStudent();
  }

  public final static String[] properties = {"key","courseStudent"};
}
