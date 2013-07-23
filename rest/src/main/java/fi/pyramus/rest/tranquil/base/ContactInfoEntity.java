package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.ContactInfo.class, entityType = TranquilModelType.COMPACT)
public class ContactInfoEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public java.util.List<Long> getAddresses_ids() {
    return addresses_ids;
  }

  public void setAddresses_ids(java.util.List<Long> addresses_ids) {
    this.addresses_ids = addresses_ids;
  }

  public java.util.List<Long> getEmails_ids() {
    return emails_ids;
  }

  public void setEmails_ids(java.util.List<Long> emails_ids) {
    this.emails_ids = emails_ids;
  }

  public java.util.List<Long> getPhoneNumbers_ids() {
    return phoneNumbers_ids;
  }

  public void setPhoneNumbers_ids(java.util.List<Long> phoneNumbers_ids) {
    this.phoneNumbers_ids = phoneNumbers_ids;
  }

  public java.util.List<Long> getContactURLs_ids() {
    return contactURLs_ids;
  }

  public void setContactURLs_ids(java.util.List<Long> contactURLs_ids) {
    this.contactURLs_ids = contactURLs_ids;
  }

  private Long id;

  private String additionalInfo;

  private Long version;

  private java.util.List<Long> addresses_ids;

  private java.util.List<Long> emails_ids;

  private java.util.List<Long> phoneNumbers_ids;

  private java.util.List<Long> contactURLs_ids;

  public final static String[] properties = {"id","additionalInfo","version","addresses","emails","phoneNumbers","contactURLs"};
}
