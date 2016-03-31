package fi.otavanopisto.pyramus.rest.model;

public class Email {

  public Email() {
    super();
  }

  public Email(Long id, Long contactTypeId, Boolean defaultAddress, String address) {
    this();
    this.id = id;
    this.contactTypeId = contactTypeId;
    this.defaultAddress = defaultAddress;
    this.address = address;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getContactTypeId() {
    return contactTypeId;
  }

  public void setContactTypeId(Long contactTypeId) {
    this.contactTypeId = contactTypeId;
  }

  public Boolean getDefaultAddress() {
    return defaultAddress;
  }

  public void setDefaultAddress(Boolean defaultAddress) {
    this.defaultAddress = defaultAddress;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  private Long id;
  private Long contactTypeId;
  private Boolean defaultAddress;
  private String address;
}
