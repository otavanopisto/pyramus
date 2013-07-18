package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.users.User.class, entityType = TranquilModelType.COMPACT)
public class UserCompact extends UserBase {

  public Long getContactInfo_id() {
    return contactInfo_id;
  }

  public void setContactInfo_id(Long contactInfo_id) {
    this.contactInfo_id = contactInfo_id;
  }

  public java.util.List<Long> getVariables_ids() {
    return variables_ids;
  }

  public void setVariables_ids(java.util.List<Long> variables_ids) {
    this.variables_ids = variables_ids;
  }

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  public java.util.List<Long> getBillingDetails_ids() {
    return billingDetails_ids;
  }

  public void setBillingDetails_ids(java.util.List<Long> billingDetails_ids) {
    this.billingDetails_ids = billingDetails_ids;
  }

  private Long contactInfo_id;

  private java.util.List<Long> variables_ids;

  private java.util.List<Long> tags_ids;

  private java.util.List<Long> billingDetails_ids;

  public final static String[] properties = {"contactInfo","variables","tags","billingDetails"};
}
