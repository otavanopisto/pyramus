package fi.pyramus.rest.model;

public class ContactURL {

  public ContactURL() {
    super();
  }

  public ContactURL(Long id, Long contactURLTypeId, String url) {
    super();
    this.id = id;
    this.contactURLTypeId = contactURLTypeId;
    this.url = url;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getContactURLTypeId() {
    return contactURLTypeId;
  }
  
  public void setContactURLTypeId(Long contactUrlTypeId) {
    this.contactURLTypeId = contactUrlTypeId;
  }
  
  public String getUrl() {
    return url;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }

  private Long id;
  private Long contactURLTypeId;
  private String url;
}
