package fi.otavanopisto.pyramus.rest.model;

public class BillingDetails {

  public BillingDetails() {
  }
  
  public BillingDetails(
      Long id, String personName, String companyName, String streetAddress1, String streetAddress2, String postalCode,
      String city, String region, String country, String phoneNumber, String emailAddress, String companyIdentifier,
      String referenceNumber, String electronicBillingAddress, String electronicBillingOperator, String notes) {
    this.id = id;
    this.personName = personName;
    this.companyName = companyName;
    this.streetAddress1 = streetAddress1;
    this.streetAddress2 = streetAddress2;
    this.postalCode = postalCode;
    this.city = city;
    this.region = region;
    this.country = country;
    this.phoneNumber = phoneNumber;
    this.emailAddress = emailAddress;
    this.companyIdentifier = companyIdentifier;
    this.referenceNumber = referenceNumber;
    this.electronicBillingAddress = electronicBillingAddress;
    this.electronicBillingOperator = electronicBillingOperator;
    this.notes = notes;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
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
  
  public String getElectronicBillingAddress() {
    return electronicBillingAddress;
  }
  
  public void setElectronicBillingAddress(String electronicBillingAddress) {
    this.electronicBillingAddress = electronicBillingAddress;
  }
  
  public String getElectronicBillingOperator() {
    return electronicBillingOperator;
  }
  
  public void setElectronicBillingOperator(String electronicBillingOperator) {
    this.electronicBillingOperator = electronicBillingOperator;
  }
  
  public String getNotes() {
    return notes;
  }
  
  public void setNotes(String notes) {
    this.notes = notes;
  }

  private Long id;
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
  private String companyIdentifier;
  private String referenceNumber;
  private String electronicBillingAddress;
  private String electronicBillingOperator;
  private String notes;
}
