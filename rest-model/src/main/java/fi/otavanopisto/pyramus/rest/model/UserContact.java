package fi.otavanopisto.pyramus.rest.model;

public class UserContact {

  public UserContact() {
  }

  public UserContact(Long id, String name, String phoneNumber, String email, String streetAddress, String postalCode, String city, String country, String contactType, Boolean defaultContact) {
    super();
    this.id = id;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.streetAddress = streetAddress;
    this.postalCode = postalCode;
    this.city = city;
    this.country = country;
    this.contactType = contactType;
    this.defaultContact = defaultContact;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getContactType() {
    return contactType;
  }

  public void setContactType(String contactType) {
    this.contactType = contactType;
  }

  public Boolean isDefaultContact() {
    return defaultContact;
  }

  public void setDefaultContact(Boolean defaultContact) {
    this.defaultContact = defaultContact;
  }

  private Long id;
  private String name;
  private String phoneNumber;
  private String email;
  private String streetAddress;
  private String postalCode;
  private String city;
  private String country;
  private String contactType;
  private Boolean defaultContact;
}
