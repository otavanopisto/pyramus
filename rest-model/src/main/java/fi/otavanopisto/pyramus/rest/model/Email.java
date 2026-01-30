package fi.otavanopisto.pyramus.rest.model;

public class Email {

  public Email() {
    super();
  }

  public Email(Long id, Boolean defaultAddress, String address) {
    this();
    this.id = id;
    this.defaultAddress = defaultAddress;
    this.address = address;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  private Long id;
  private Boolean defaultAddress;
  private String address;
}
