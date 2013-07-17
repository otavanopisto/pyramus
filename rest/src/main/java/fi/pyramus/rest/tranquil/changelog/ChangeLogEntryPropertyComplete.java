package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryProperty.class, entityType = TranquilModelType.COMPLETE)
public class ChangeLogEntryPropertyComplete extends ChangeLogEntryPropertyBase {

  public TranquilModelEntity getProperty() {
    return property;
  }

  public void setProperty(TranquilModelEntity property) {
    this.property = property;
  }

  public TranquilModelEntity getEntry() {
    return entry;
  }

  public void setEntry(TranquilModelEntity entry) {
    this.entry = entry;
  }

  private TranquilModelEntity property;

  private TranquilModelEntity entry;

  public final static String[] properties = {"property","entry"};
}
