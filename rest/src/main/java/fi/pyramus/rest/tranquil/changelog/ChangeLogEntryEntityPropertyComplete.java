package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty.class, entityType = TranquilModelType.COMPLETE)
public class ChangeLogEntryEntityPropertyComplete extends ChangeLogEntryEntityPropertyBase {

  public TranquilModelEntity getEntity() {
    return entity;
  }

  public void setEntity(TranquilModelEntity entity) {
    this.entity = entity;
  }

  private TranquilModelEntity entity;

  public final static String[] properties = {"entity"};
}
