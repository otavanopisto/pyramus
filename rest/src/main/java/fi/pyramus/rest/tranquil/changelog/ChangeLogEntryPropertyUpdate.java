package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryProperty.class, entityType = TranquilModelType.UPDATE)
public class ChangeLogEntryPropertyUpdate extends ChangeLogEntryPropertyComplete {

  public void setProperty(ChangeLogEntryEntityPropertyCompact property) {
    super.setProperty(property);
  }

  public ChangeLogEntryEntityPropertyCompact getProperty() {
    return (ChangeLogEntryEntityPropertyCompact)super.getProperty();
  }

  public void setEntry(ChangeLogEntryCompact entry) {
    super.setEntry(entry);
  }

  public ChangeLogEntryCompact getEntry() {
    return (ChangeLogEntryCompact)super.getEntry();
  }

  public final static String[] properties = {"property","entry"};
}
