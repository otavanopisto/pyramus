package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.BillingDetails.class, entityType = TranquilModelType.BASE)
public class BillingDetailsBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getCompanyIdentifier() {
    return companyIdentifier;
  }

  public void setCompanyIdentifier(String companyIdentifier) {
    this.companyIdentifier = companyIdentifier;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public String getPersonName() {
    return personName;
  }

  public void setPersonName(String personName) {
    this.personName = personName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getStreetAddress1() {
    return streetAddress1;
  }

  public void setStreetAddress1(String streetAddress1) {
    this.streetAddress1 = streetAddress1;
  }

  public String getStreetAddress2() {
    return streetAddress2;
  }

  public void setStreetAddress2(String streetAddress2) {
    this.streetAddress2 = streetAddress2;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  private Long id;

  private String bic;

  private String iban;

  private String companyIdentifier;

  private String referenceNumber;

  private String personName;

  private String companyName;

  private String streetAddress1;

  private String streetAddress2;

  private String postalCode;

  private String city;

  private String region;

  private String country;

  private String phoneNumber;

  private String emailAddress;

  public final static String[] properties = {"id","bic","iban","companyIdentifier","referenceNumber","personName","companyName","streetAddress1","streetAddress2","postalCode","city","region","country","phoneNumber","emailAddress"};
}
