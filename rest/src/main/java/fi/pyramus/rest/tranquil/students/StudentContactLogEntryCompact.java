package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentContactLogEntry.class, entityType = TranquilModelType.COMPACT)
public class StudentContactLogEntryCompact extends StudentContactLogEntryBase {

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  private Long student_id;

  public final static String[] properties = {"student"};
}
