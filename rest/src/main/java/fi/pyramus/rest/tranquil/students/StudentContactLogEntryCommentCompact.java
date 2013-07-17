package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentContactLogEntryComment.class, entityType = TranquilModelType.COMPACT)
public class StudentContactLogEntryCommentCompact extends StudentContactLogEntryCommentBase {

  public Long getEntry_id() {
    return entry_id;
  }

  public void setEntry_id(Long entry_id) {
    this.entry_id = entry_id;
  }

  private Long entry_id;

  public final static String[] properties = {"entry"};
}
