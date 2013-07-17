package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntry.class, entityType = TranquilModelType.COMPLETE)
public class ChangeLogEntryComplete extends ChangeLogEntryBase {

  public TranquilModelEntity getEntity() {
    return entity;
  }

  public void setEntity(TranquilModelEntity entity) {
    this.entity = entity;
  }

  public TranquilModelEntity getUser() {
    return user;
  }

  public void setUser(TranquilModelEntity user) {
    this.user = user;
  }

  private TranquilModelEntity entity;

  private TranquilModelEntity user;

  public final static String[] properties = {"entity","user"};
}
