package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Address.class, entityType = TranquilModelType.COMPACT)
public class AddressEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getDefaultAddress() {
    return defaultAddress;
  }

  public void setDefaultAddress(Boolean defaultAddress) {
    this.defaultAddress = defaultAddress;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getContactType_id() {
    return contactType_id;
  }

  public void setContactType_id(Long contactType_id) {
    this.contactType_id = contactType_id;
  }

  public Long getContactInfo_id() {
    return contactInfo_id;
  }

  public void setContactInfo_id(Long contactInfo_id) {
    this.contactInfo_id = contactInfo_id;
  }

  private Long id;

  private String postalCode;

  private String streetAddress;

  private String country;

  private String city;

  private String name;

  private Boolean defaultAddress;

  private Long version;

  private Long contactType_id;

  private Long contactInfo_id;

  public final static String[] properties = {"id","postalCode","streetAddress","country","city","name","defaultAddress","version","contactType","contactInfo"};
}
