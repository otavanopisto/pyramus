package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.users.User.class, entityType = TranquilModelType.COMPLETE)
public class UserComplete extends UserBase {

  public TranquilModelEntity getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(TranquilModelEntity contactInfo) {
    this.contactInfo = contactInfo;
  }

  public java.util.List<TranquilModelEntity> getVariables() {
    return variables;
  }

  public void setVariables(java.util.List<TranquilModelEntity> variables) {
    this.variables = variables;
  }

  public java.util.List<TranquilModelEntity> getTags() {
    return tags;
  }

  public void setTags(java.util.List<TranquilModelEntity> tags) {
    this.tags = tags;
  }

  public java.util.List<TranquilModelEntity> getBillingDetails() {
    return billingDetails;
  }

  public void setBillingDetails(java.util.List<TranquilModelEntity> billingDetails) {
    this.billingDetails = billingDetails;
  }

  private TranquilModelEntity contactInfo;

  private java.util.List<TranquilModelEntity> variables;

  private java.util.List<TranquilModelEntity> tags;

  private java.util.List<TranquilModelEntity> billingDetails;

  public final static String[] properties = {"contactInfo","variables","tags","billingDetails"};
}
