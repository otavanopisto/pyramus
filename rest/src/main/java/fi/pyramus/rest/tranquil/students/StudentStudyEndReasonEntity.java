package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentStudyEndReason.class, entityType = TranquilModelType.COMPACT)
public class StudentStudyEndReasonEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

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

  private Long id;

  private String name;

  private Long version;

  private Long parentReason_id;

  private java.util.List<Long> childEndReasons_ids;

  public final static String[] properties = {"id","name","version","parentReason","childEndReasons"};
}
