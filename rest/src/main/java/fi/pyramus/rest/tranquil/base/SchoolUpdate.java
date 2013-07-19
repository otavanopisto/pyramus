package fi.pyramus.domainmodel.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.School.class, entityType = TranquilModelType.UPDATE)
public class SchoolUpdate extends SchoolComplete {

  public void setContactInfo(ContactInfoCompact contactInfo) {
    super.setContactInfo(contactInfo);
  }

  public ContactInfoCompact getContactInfo() {
    return (ContactInfoCompact)super.getContactInfo();
  }

  public void setField(SchoolFieldCompact field) {
    super.setField(field);
  }

  public SchoolFieldCompact getField() {
    return (SchoolFieldCompact)super.getField();
  }

  public final static String[] properties = {"contactInfo","field"};
}
