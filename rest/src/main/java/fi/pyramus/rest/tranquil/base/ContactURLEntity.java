package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.ContactURL.class, entityType = TranquilModelType.COMPACT)
public class ContactURLEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getURL() {
    return uRL;
  }

  public void setURL(String uRL) {
    this.uRL = uRL;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getContactURLType_id() {
    return contactURLType_id;
  }

  public void setContactURLType_id(Long contactURLType_id) {
    this.contactURLType_id = contactURLType_id;
  }

  public Long getContactInfo_id() {
    return contactInfo_id;
  }

  public void setContactInfo_id(Long contactInfo_id) {
    this.contactInfo_id = contactInfo_id;
  }

  private Long id;

  private String uRL;

  private Long version;

  private Long contactURLType_id;

  private Long contactInfo_id;

  public final static String[] properties = {"id","uRL","version","contactURLType","contactInfo"};
}
