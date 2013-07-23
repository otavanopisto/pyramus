package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntry.class, entityType = TranquilModelType.COMPACT)
public class ChangeLogEntryEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getEntity_id() {
    return entity_id;
  }

  public void setEntity_id(Long entity_id) {
    this.entity_id = entity_id;
  }

  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  private Long id;

  private fi.pyramus.domainmodel.changelog.ChangeLogEntryType type;

  private String entityId;

  private java.util.Date time;

  private Long entity_id;

  private Long user_id;

  public final static String[] properties = {"id","type","entityId","time","entity","user"};
}
