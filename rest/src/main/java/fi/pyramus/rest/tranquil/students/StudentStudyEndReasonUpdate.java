package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentStudyEndReason.class, entityType = TranquilModelType.UPDATE)
public class StudentStudyEndReasonUpdate extends StudentStudyEndReasonComplete {

  public void setParentReason(StudentStudyEndReasonCompact parentReason) {
    super.setParentReason(parentReason);
  }

  public StudentStudyEndReasonCompact getParentReason() {
    return (StudentStudyEndReasonCompact)super.getParentReason();
  }

  public void setChildEndReasons(java.util.List<StudentStudyEndReasonCompact> childEndReasons) {
    super.setChildEndReasons(childEndReasons);
  }

  public java.util.List<StudentStudyEndReasonCompact> getChildEndReasons() {
    return (java.util.List<StudentStudyEndReasonCompact>)super.getChildEndReasons();
  }

  public final static String[] properties = {"parentReason","childEndReasons"};
}
