package fi.otavanopisto.pyramus.rest.model;

public class UserContact {

  public UserContact() {
  }

  public UserContact(String name, String phoneNumber, String email, String address, String contactType) {
    super();
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.address = address;
    this.contactType = contactType;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContactType() {
    return contactType;
  }

  public void setContactType(String contactType) {
    this.contactType = contactType;
  }

  private String name;
  private String phoneNumber;
  private String email;
  private String address;
  private String contactType;
}
