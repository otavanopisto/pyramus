package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Email.class, entityType = TranquilModelType.COMPACT)
public class EmailEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
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

  private String address;

  private Boolean defaultAddress;

  private Long version;

  private Long contactType_id;

  private Long contactInfo_id;

  public final static String[] properties = {"id","address","defaultAddress","version","contactType","contactInfo"};
}
