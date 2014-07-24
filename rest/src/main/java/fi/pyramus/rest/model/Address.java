package fi.pyramus.rest.model;

public class Address {

  public Address() {
    super();
  }

  public Address(Long id, Long contactTypeId, Boolean defaultAddress, String name, String streetAddress, String postalCode, String city, String country) {
    this();
    this.id = id;
    this.defaultAddress = defaultAddress;
    this.contactTypeId = contactTypeId;
    this.name = name;
    this.streetAddress = streetAddress;
    this.postalCode = postalCode;
    this.city = city;
    this.country = country;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getDefaultAddress() {
    return defaultAddress;
  }

  public void setDefaultAddress(Boolean defaultAddress) {
    this.defaultAddress = defaultAddress;
  }

  public Long getContactTypeId() {
    return contactTypeId;
  }

  public void setContactTypeId(Long contactTypeId) {
    this.contactTypeId = contactTypeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  private Long id;
  private Boolean defaultAddress;
  private Long contactTypeId;
  private String name;
  private String streetAddress;
  private String postalCode;
  private String city;
  private String country;
}
