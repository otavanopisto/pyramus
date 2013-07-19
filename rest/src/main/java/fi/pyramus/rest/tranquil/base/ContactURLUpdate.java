package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ContactURL.class, entityType = TranquilModelType.UPDATE)
public class ContactURLUpdate extends ContactURLComplete {

  public void setContactURLType(ContactURLTypeCompact contactURLType) {
    super.setContactURLType(contactURLType);
  }

  public ContactURLTypeCompact getContactURLType() {
    return (ContactURLTypeCompact)super.getContactURLType();
  }

  public void setContactInfo(ContactInfoCompact contactInfo) {
    super.setContactInfo(contactInfo);
  }

  public ContactInfoCompact getContactInfo() {
    return (ContactInfoCompact)super.getContactInfo();
  }

  public final static String[] properties = {"contactURLType","contactInfo"};
}
