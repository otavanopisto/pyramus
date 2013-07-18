package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.School.class, entityType = TranquilModelType.COMPACT)
public class SchoolCompact extends SchoolBase {

  public Long getContactInfo_id() {
    return contactInfo_id;
  }

  public void setContactInfo_id(Long contactInfo_id) {
    this.contactInfo_id = contactInfo_id;
  }

  public Long getField_id() {
    return field_id;
  }

  public void setField_id(Long field_id) {
    this.field_id = field_id;
  }

  private Long contactInfo_id;

  private Long field_id;

  public final static String[] properties = {"contactInfo","field"};
}
