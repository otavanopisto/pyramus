package fi.otavanopisto.pyramus.rest.model;

public class PhoneNumber {

  public PhoneNumber() {
    super();
  }

  public PhoneNumber(Long id, Long contactTypeId, Boolean defaultNumber, String number) {
    this();
    this.id = id;
    this.contactTypeId = contactTypeId;
    this.defaultNumber = defaultNumber;
    this.number = number;
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
  
  public Boolean getDefaultNumber() {
    return defaultNumber;
  }
  
  public void setDefaultNumber(Boolean defaultNumber) {
    this.defaultNumber = defaultNumber;
  }
  
  public String getNumber() {
    return number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }

  private Long id;
  private Long contactTypeId;
  private Boolean defaultNumber;
  private String number;
}
