package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentStudyEndReason.class, entityType = TranquilModelType.COMPLETE)
public class StudentStudyEndReasonComplete extends StudentStudyEndReasonBase {

  public TranquilModelEntity getParentReason() {
    return parentReason;
  }

  public void setParentReason(TranquilModelEntity parentReason) {
    this.parentReason = parentReason;
  }

  public java.util.List<TranquilModelEntity> getChildEndReasons() {
    return childEndReasons;
  }

  public void setChildEndReasons(java.util.List<TranquilModelEntity> childEndReasons) {
    this.childEndReasons = childEndReasons;
  }

  private TranquilModelEntity parentReason;

  private java.util.List<TranquilModelEntity> childEndReasons;

  public final static String[] properties = {"parentReason","childEndReasons"};
}
