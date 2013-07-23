package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ContactInfo.class, entityType = TranquilModelType.COMPLETE)
public class ContactInfoComplete extends ContactInfoBase {

  public java.util.List<TranquilModelEntity> getAddresses() {
    return addresses;
  }

  public void setAddresses(java.util.List<TranquilModelEntity> addresses) {
    this.addresses = addresses;
  }

  public java.util.List<TranquilModelEntity> getEmails() {
    return emails;
  }

  public void setEmails(java.util.List<TranquilModelEntity> emails) {
    this.emails = emails;
  }

  public java.util.List<TranquilModelEntity> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(java.util.List<TranquilModelEntity> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public java.util.List<TranquilModelEntity> getContactURLs() {
    return contactURLs;
  }

  public void setContactURLs(java.util.List<TranquilModelEntity> contactURLs) {
    this.contactURLs = contactURLs;
  }

  private java.util.List<TranquilModelEntity> addresses;

  private java.util.List<TranquilModelEntity> emails;

  private java.util.List<TranquilModelEntity> phoneNumbers;

  private java.util.List<TranquilModelEntity> contactURLs;

  public final static String[] properties = {"addresses","emails","phoneNumbers","contactURLs"};
}
