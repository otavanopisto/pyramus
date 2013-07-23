package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ContactURL.class, entityType = TranquilModelType.COMPLETE)
public class ContactURLComplete extends ContactURLBase {

  public TranquilModelEntity getContactURLType() {
    return contactURLType;
  }

  public void setContactURLType(TranquilModelEntity contactURLType) {
    this.contactURLType = contactURLType;
  }

  public TranquilModelEntity getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(TranquilModelEntity contactInfo) {
    this.contactInfo = contactInfo;
  }

  private TranquilModelEntity contactURLType;

  private TranquilModelEntity contactInfo;

  public final static String[] properties = {"contactURLType","contactInfo"};
}
