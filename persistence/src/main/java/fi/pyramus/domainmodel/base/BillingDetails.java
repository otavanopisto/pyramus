package fi.pyramus.domainmodel.base;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class BillingDetails {

  public Long getId() {
    return id;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public String getBic() {
    return bic;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getIban() {
    return iban;
  }

  public void setCompanyIdentifier(String companyIdentifier) {
    this.companyIdentifier = companyIdentifier;
  }

  public String getCompanyIdentifier() {
    return companyIdentifier;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setPersonName(String personName) {
    this.personName = personName;
  }

  public String getPersonName() {
    return personName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setStreetAddress1(String streetAddress1) {
    this.streetAddress1 = streetAddress1;
  }

  public String getStreetAddress1() {
    return streetAddress1;
  }

  public void setStreetAddress2(String streetAddress2) {
    this.streetAddress2 = streetAddress2;
  }

  public String getStreetAddress2() {
    return streetAddress2;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCity() {
    return city;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getRegion() {
    return region;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountry() {
    return country;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="BillingDetails")  
  @TableGenerator(name="BillingDetails", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @Field (store = Store.NO)
  private String personName;

  @Field (store = Store.NO)
  private String companyName;
  
  @Field (store = Store.NO)
  private String streetAddress1;

  @Field (store = Store.NO)
  private String streetAddress2;

  @Field (store = Store.NO)
  private String postalCode;

  @Field (store = Store.NO)
  private String city;

  @Field (store = Store.NO)
  private String region;
  
  @Field (store = Store.NO)
  private String country;

  @Field (store = Store.NO)
  private String phoneNumber;

  @Field (store = Store.NO)
  // TODO Email annotation?
  private String emailAddress;

  @Field (store = Store.NO)
  private String bic;

  @Field (store = Store.NO)
  private String iban;
  
  @Field (store = Store.NO)
  private String companyIdentifier;

  @Field (store = Store.NO)
  private String referenceNumber;

}
