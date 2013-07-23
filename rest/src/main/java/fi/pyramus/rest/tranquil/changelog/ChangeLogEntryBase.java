package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntry.class, entityType = TranquilModelType.BASE)
public class ChangeLogEntryBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public fi.pyramus.domainmodel.changelog.ChangeLogEntryType getType() {
    return type;
  }

  public void setType(fi.pyramus.domainmodel.changelog.ChangeLogEntryType type) {
    this.type = type;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public java.util.Date getTime() {
    return time;
  }

  public void setTime(java.util.Date time) {
    this.time = time;
  }

  private Long id;

  private fi.pyramus.domainmodel.changelog.ChangeLogEntryType type;

  private String entityId;

  private java.util.Date time;

  public final static String[] properties = {"id","type","entityId","time"};
}
