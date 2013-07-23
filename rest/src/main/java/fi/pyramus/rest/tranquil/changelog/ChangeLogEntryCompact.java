package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntry.class, entityType = TranquilModelType.COMPACT)
public class ChangeLogEntryCompact extends ChangeLogEntryBase {

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

  private Long entity_id;

  private Long user_id;

  public final static String[] properties = {"entity","user"};
}
