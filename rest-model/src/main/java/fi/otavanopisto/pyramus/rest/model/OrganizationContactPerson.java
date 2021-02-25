package fi.otavanopisto.pyramus.rest.model;

public class OrganizationContactPerson {

  public OrganizationContactPerson() {
  }
  
  public OrganizationContactPerson(Long id, OrganizationContactPersonType type, String name, String email, String phone, String title) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.title = title;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public OrganizationContactPersonType getType() {
    return type;
  }
  
  public void setType(OrganizationContactPersonType type) {
    this.type = type;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getPhone() {
    return phone;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  private Long id;
  private OrganizationContactPersonType type;
  private String name;
  private String email;
  private String phone;
  private String title;
}
