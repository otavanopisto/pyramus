package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Email.class, entityType = TranquilModelType.COMPACT)
public class EmailCompact extends EmailBase {

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

  private Long contactType_id;

  private Long contactInfo_id;

  public final static String[] properties = {"contactType","contactInfo"};
}
