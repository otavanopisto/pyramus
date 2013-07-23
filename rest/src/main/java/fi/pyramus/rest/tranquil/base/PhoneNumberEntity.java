package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.PhoneNumber.class, entityType = TranquilModelType.COMPACT)
public class PhoneNumberEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  private Boolean defaultNumber;

  private String number;

  private Long version;

  private Long contactType_id;

  private Long contactInfo_id;

  public final static String[] properties = {"id","defaultNumber","number","version","contactType","contactInfo"};
}
