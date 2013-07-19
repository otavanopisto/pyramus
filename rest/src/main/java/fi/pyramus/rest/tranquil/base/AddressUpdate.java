package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Address.class, entityType = TranquilModelType.UPDATE)
public class AddressUpdate extends AddressComplete {

  public void setContactType(ContactTypeCompact contactType) {
    super.setContactType(contactType);
  }

  public ContactTypeCompact getContactType() {
    return (ContactTypeCompact)super.getContactType();
  }

  public void setContactInfo(ContactInfoCompact contactInfo) {
    super.setContactInfo(contactInfo);
  }

  public ContactInfoCompact getContactInfo() {
    return (ContactInfoCompact)super.getContactInfo();
  }

  public final static String[] properties = {"contactType","contactInfo"};
}
