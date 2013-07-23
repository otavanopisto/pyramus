package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.users.User.class, entityType = TranquilModelType.UPDATE)
public class UserUpdate extends UserComplete {

  public void setContactInfo(ContactInfoCompact contactInfo) {
    super.setContactInfo(contactInfo);
  }

  public ContactInfoCompact getContactInfo() {
    return (ContactInfoCompact)super.getContactInfo();
  }

  public void setVariables(java.util.List<UserVariableCompact> variables) {
    super.setVariables(variables);
  }

  public java.util.List<UserVariableCompact> getVariables() {
    return (java.util.List<UserVariableCompact>)super.getVariables();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public void setBillingDetails(java.util.List<BillingDetailsCompact> billingDetails) {
    super.setBillingDetails(billingDetails);
  }

  public java.util.List<BillingDetailsCompact> getBillingDetails() {
    return (java.util.List<BillingDetailsCompact>)super.getBillingDetails();
  }

  public final static String[] properties = {"contactInfo","variables","tags","billingDetails"};
}
