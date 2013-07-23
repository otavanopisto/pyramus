package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentContactLogEntry.class, entityType = TranquilModelType.COMPLETE)
public class StudentContactLogEntryComplete extends StudentContactLogEntryBase {

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  private TranquilModelEntity student;

  public final static String[] properties = {"student"};
}
