package fi.otavanopisto.pyramus.services.entities.base;

public class AddressEntity {

  public AddressEntity() {
  }
  
  public AddressEntity(Long id, Boolean defaultAddress, Long contactTypeId, String country, String city, String postalCode, String streetAddress) {
    this.id = id;
    this.defaultAddress = defaultAddress;
    this.contactTypeId = contactTypeId;
    this.country = country;
    this.city = city;
    this.postalCode = postalCode;
    this.streetAddress = streetAddress;
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  private Long id;
  private Boolean defaultAddress;
  private Long contactTypeId;
  private String country;
  private String city;
  private String postalCode;
  private String streetAddress;
}
