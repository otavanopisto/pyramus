package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ContactInfo.class, entityType = TranquilModelType.UPDATE)
public class ContactInfoUpdate extends ContactInfoComplete {

  public void setAddresses(java.util.List<AddressCompact> addresses) {
    super.setAddresses(addresses);
  }

  public java.util.List<AddressCompact> getAddresses() {
    return (java.util.List<AddressCompact>)super.getAddresses();
  }

  public void setEmails(java.util.List<EmailCompact> emails) {
    super.setEmails(emails);
  }

  public java.util.List<EmailCompact> getEmails() {
    return (java.util.List<EmailCompact>)super.getEmails();
  }

  public void setPhoneNumbers(java.util.List<PhoneNumberCompact> phoneNumbers) {
    super.setPhoneNumbers(phoneNumbers);
  }

  public java.util.List<PhoneNumberCompact> getPhoneNumbers() {
    return (java.util.List<PhoneNumberCompact>)super.getPhoneNumbers();
  }

  public void setContactURLs(java.util.List<ContactURLCompact> contactURLs) {
    super.setContactURLs(contactURLs);
  }

  public java.util.List<ContactURLCompact> getContactURLs() {
    return (java.util.List<ContactURLCompact>)super.getContactURLs();
  }

  public final static String[] properties = {"addresses","emails","phoneNumbers","contactURLs"};
}
