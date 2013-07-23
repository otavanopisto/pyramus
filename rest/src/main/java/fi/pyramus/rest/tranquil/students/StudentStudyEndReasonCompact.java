package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentStudyEndReason.class, entityType = TranquilModelType.COMPACT)
public class StudentStudyEndReasonCompact extends StudentStudyEndReasonBase {

  public Long getParentReason_id() {
    return parentReason_id;
  }

  public void setParentReason_id(Long parentReason_id) {
    this.parentReason_id = parentReason_id;
  }

  public java.util.List<Long> getChildEndReasons_ids() {
    return childEndReasons_ids;
  }

  public void setChildEndReasons_ids(java.util.List<Long> childEndReasons_ids) {
    this.childEndReasons_ids = childEndReasons_ids;
  }

  private Long parentReason_id;

  private java.util.List<Long> childEndReasons_ids;

  public final static String[] properties = {"parentReason","childEndReasons"};
}
