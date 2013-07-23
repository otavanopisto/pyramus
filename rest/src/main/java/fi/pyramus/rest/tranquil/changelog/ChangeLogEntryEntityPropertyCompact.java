package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty.class, entityType = TranquilModelType.COMPACT)
public class ChangeLogEntryEntityPropertyCompact extends ChangeLogEntryEntityPropertyBase {

  public Long getEntity_id() {
    return entity_id;
  }

  public void setEntity_id(Long entity_id) {
    this.entity_id = entity_id;
  }

  private Long entity_id;

  public final static String[] properties = {"entity"};
}
