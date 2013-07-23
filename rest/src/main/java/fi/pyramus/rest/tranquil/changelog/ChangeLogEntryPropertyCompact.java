package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryProperty.class, entityType = TranquilModelType.COMPACT)
public class ChangeLogEntryPropertyCompact extends ChangeLogEntryPropertyBase {

  public Long getProperty_id() {
    return property_id;
  }

  public void setProperty_id(Long property_id) {
    this.property_id = property_id;
  }

  public Long getEntry_id() {
    return entry_id;
  }

  public void setEntry_id(Long entry_id) {
    this.entry_id = entry_id;
  }

  private Long property_id;

  private Long entry_id;

  public final static String[] properties = {"property","entry"};
}
