package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Address.class, entityType = TranquilModelType.COMPLETE)
public class AddressComplete extends AddressBase {

  public TranquilModelEntity getContactType() {
    return contactType;
  }

  public void setContactType(TranquilModelEntity contactType) {
    this.contactType = contactType;
  }

  public TranquilModelEntity getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(TranquilModelEntity contactInfo) {
    this.contactInfo = contactInfo;
  }

  private TranquilModelEntity contactType;

  private TranquilModelEntity contactInfo;

  public final static String[] properties = {"contactType","contactInfo"};
}
