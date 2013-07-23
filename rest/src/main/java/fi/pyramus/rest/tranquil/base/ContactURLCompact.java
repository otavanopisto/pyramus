package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.ContactURL.class, entityType = TranquilModelType.COMPACT)
public class ContactURLCompact extends ContactURLBase {

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

  private Long contactURLType_id;

  private Long contactInfo_id;

  public final static String[] properties = {"contactURLType","contactInfo"};
}
