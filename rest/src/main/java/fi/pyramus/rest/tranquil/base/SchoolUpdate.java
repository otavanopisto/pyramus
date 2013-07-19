package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.School.class, entityType = TranquilModelType.UPDATE)
public class SchoolUpdate extends SchoolComplete {

  public void setContactInfo(ContactInfoCompact contactInfo) {
    this.contactInfo = contactInfo;
  }

  public ContactInfoCompact getContactInfo() {
    return contactInfo;
  }

  public void setField(SchoolFieldCompact field) {
    this.field = field;
  }

  public SchoolFieldCompact getField() {
    return field;
  }

  public final static String[] properties = {"contactInfo","field"};
}
