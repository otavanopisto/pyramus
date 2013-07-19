package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntry.class, entityType = TranquilModelType.UPDATE)
public class ChangeLogEntryUpdate extends ChangeLogEntryComplete {

  public void setEntity(ChangeLogEntryEntityCompact entity) {
    super.setEntity(entity);
  }

  public ChangeLogEntryEntityCompact getEntity() {
    return (ChangeLogEntryEntityCompact)super.getEntity();
  }

  public void setUser(UserCompact user) {
    super.setUser(user);
  }

  public UserCompact getUser() {
    return (UserCompact)super.getUser();
  }

  public final static String[] properties = {"entity","user"};
}
