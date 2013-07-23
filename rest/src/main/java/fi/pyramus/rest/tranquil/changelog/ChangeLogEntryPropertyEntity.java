package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryProperty.class, entityType = TranquilModelType.COMPACT)
public class ChangeLogEntryPropertyEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

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

  private Long id;

  private String value;

  private Long property_id;

  private Long entry_id;

  public final static String[] properties = {"id","value","property","entry"};
}
