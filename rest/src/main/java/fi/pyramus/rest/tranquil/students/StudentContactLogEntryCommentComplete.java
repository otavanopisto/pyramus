package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentContactLogEntryComment.class, entityType = TranquilModelType.COMPLETE)
public class StudentContactLogEntryCommentComplete extends StudentContactLogEntryCommentBase {

  public TranquilModelEntity getEntry() {
    return entry;
  }

  public void setEntry(TranquilModelEntity entry) {
    this.entry = entry;
  }

  private TranquilModelEntity entry;

  public final static String[] properties = {"entry"};
}
