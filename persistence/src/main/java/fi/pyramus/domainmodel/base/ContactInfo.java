package fi.pyramus.domainmodel.base;

import java.util.List;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
@Indexed
public class ContactInfo {

  public Long getId() {
    return id;
  }

  @SuppressWarnings("unused")
  private void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  public List<Address> getAddresses() {
    return addresses;
  }

  public void addAddress(Address address) {
    if (address.getContactInfo() != null) {
      address.getContactInfo().removeAddress(address);
    }
    address.setContactInfo(this);
    addresses.add(address);
  }

  public void removeAddress(Address address) {
    address.setContactInfo(null);
    addresses.remove(address);
  } 

  @SuppressWarnings("unused")
  private void setEmails(List<Email> emails) {
    this.emails = emails;
  }

  public List<Email> getEmails() {
    return emails;
  }

  public void addEmail(Email email) {
    if (email.getContactInfo() != null) {
      email.getContactInfo().removeEmail(email);
    }
    email.setContactInfo(this);
    emails.add(email);
  }

  public void removeEmail(Email email) {
    email.setContactInfo(null);
    emails.remove(email);
  } 

  @SuppressWarnings("unused")
  private void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public List<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void addPhoneNumber(PhoneNumber phoneNumber) {
    if (phoneNumber.getContactInfo() != null) {
      phoneNumber.getContactInfo().removePhoneNumber(phoneNumber);
    }
    phoneNumber.setContactInfo(this);
    phoneNumbers.add(phoneNumber);
  }

  public void removePhoneNumber(PhoneNumber phoneNumber) {
    phoneNumber.setContactInfo(null);
    phoneNumbers.remove(phoneNumber);
  } 

  @SuppressWarnings("unused")
  private void setContactURLs(List<ContactURL> contactURLs) {
    this.contactURLs = contactURLs;
  }

  public List<ContactURL> getContactURLs() {
    return contactURLs;
  }

  public void addContactURL(ContactURL contactURL) {
    if (contactURL.getContactInfo() != null) {
      contactURL.getContactInfo().removeContactURL(contactURL);
    }
    contactURL.setContactInfo(this);
    contactURLs.add(contactURL);
  }

  public void removeContactURL(ContactURL contactURL) {
    contactURL.setContactInfo(null);
    contactURLs.remove(contactURL);
  } 

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ContactInfo")  
  @TableGenerator(name="ContactInfo", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="contactInfo")
  @IndexedEmbedded
  private List<Address> addresses = new Vector<Address>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="contactInfo")
  @IndexedEmbedded
  private List<Email> emails = new Vector<Email>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="contactInfo")
  @IndexedEmbedded
  private List<PhoneNumber> phoneNumbers = new Vector<PhoneNumber>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="contactInfo")
  @IndexedEmbedded
  private List<ContactURL> contactURLs = new Vector<ContactURL>();

  @Lob  
  @Basic (fetch = FetchType.LAZY)
  @Column
  private String additionalInfo;
 
  @Version
  @Column(nullable = false)
  private Long version;
}
