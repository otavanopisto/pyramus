package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.School.class, entityType = TranquilModelType.COMPLETE)
public class SchoolComplete extends SchoolBase {

  public TranquilModelEntity getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(TranquilModelEntity contactInfo) {
    this.contactInfo = contactInfo;
  }

  public TranquilModelEntity getField() {
    return field;
  }

  public void setField(TranquilModelEntity field) {
    this.field = field;
  }

  private TranquilModelEntity contactInfo;

  private TranquilModelEntity field;

  public final static String[] properties = {"contactInfo","field"};
}
