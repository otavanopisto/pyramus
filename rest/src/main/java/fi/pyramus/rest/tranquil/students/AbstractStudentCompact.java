package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.AbstractStudent.class, entityType = TranquilModelType.COMPACT)
public class AbstractStudentCompact extends AbstractStudentBase {

  public Long getLatestStudent_id() {
    return latestStudent_id;
  }

  public void setLatestStudent_id(Long latestStudent_id) {
    this.latestStudent_id = latestStudent_id;
  }

  public java.util.List<Long> getStudents_ids() {
    return students_ids;
  }

  public void setStudents_ids(java.util.List<Long> students_ids) {
    this.students_ids = students_ids;
  }

  private Long latestStudent_id;

  private java.util.List<Long> students_ids;

  public final static String[] properties = {"latestStudent","students"};
}
