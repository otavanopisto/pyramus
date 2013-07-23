package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.users.User.class, entityType = TranquilModelType.COMPACT)
public class UserEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(String authProvider) {
    this.authProvider = authProvider;
  }

  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public fi.pyramus.domainmodel.users.Role getRole() {
    return role;
  }

  public void setRole(fi.pyramus.domainmodel.users.Role role) {
    this.role = role;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

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

  private Long id;

  private String firstName;

  private String lastName;

  private String authProvider;

  private String externalId;

  private fi.pyramus.domainmodel.users.Role role;

  private Long version;

  private String title;

  private Long contactInfo_id;

  private java.util.List<Long> variables_ids;

  private java.util.List<Long> tags_ids;

  private java.util.List<Long> billingDetails_ids;

  public final static String[] properties = {"id","firstName","lastName","authProvider","externalId","role","version","title","contactInfo","variables","tags","billingDetails"};
}
