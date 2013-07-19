package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentContactLogEntryComment.class, entityType = TranquilModelType.UPDATE)
public class StudentContactLogEntryCommentUpdate extends StudentContactLogEntryCommentComplete {

  public void setEntry(StudentContactLogEntryCompact entry) {
    super.setEntry(entry);
  }

  public StudentContactLogEntryCompact getEntry() {
    return (StudentContactLogEntryCompact)super.getEntry();
  }

  public final static String[] properties = {"entry"};
}
