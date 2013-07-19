package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty.class, entityType = TranquilModelType.UPDATE)
public class ChangeLogEntryEntityPropertyUpdate extends ChangeLogEntryEntityPropertyComplete {

  public void setEntity(ChangeLogEntryEntityCompact entity) {
    super.setEntity(entity);
  }

  public ChangeLogEntryEntityCompact getEntity() {
    return (ChangeLogEntryEntityCompact)super.getEntity();
  }

  public final static String[] properties = {"entity"};
}
