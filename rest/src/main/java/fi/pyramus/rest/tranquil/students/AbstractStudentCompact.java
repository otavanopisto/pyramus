package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.AbstractStudent.class, entityType = TranquilModelType.COMPACT)
public class AbstractStudentCompact extends AbstractStudentBase {

  public java.util.List<Long> getStudents_ids() {
    return students_ids;
  }

  public void setStudents_ids(java.util.List<Long> students_ids) {
    this.students_ids = students_ids;
  }

  private java.util.List<Long> students_ids;

  public final static String[] properties = {"students"};
}
