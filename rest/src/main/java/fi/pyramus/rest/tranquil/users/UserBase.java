package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.users.User.class, entityType = TranquilModelType.BASE)
public class UserBase implements fi.tranquil.TranquilModelEntity {

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

  public String getFirstNameSortable() {
    return firstNameSortable;
  }

  public void setFirstNameSortable(String firstNameSortable) {
    this.firstNameSortable = firstNameSortable;
  }

  public String getLastNameSortable() {
    return lastNameSortable;
  }

  public void setLastNameSortable(String lastNameSortable) {
    this.lastNameSortable = lastNameSortable;
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

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public fi.pyramus.domainmodel.users.Role getRole() {
    return role;
  }

  public void setRole(fi.pyramus.domainmodel.users.Role role) {
    this.role = role;
  }

  public java.util.Set<fi.pyramus.domainmodel.base.Tag> getTags() {
    return tags;
  }

  public void setTags(java.util.Set<fi.pyramus.domainmodel.base.Tag> tags) {
    this.tags = tags;
  }

  public java.util.Map<java.lang.String,java.lang.String> getVariablesAsStringMap() {
    return variablesAsStringMap;
  }

  public void setVariablesAsStringMap(java.util.Map<java.lang.String,java.lang.String> variablesAsStringMap) {
    this.variablesAsStringMap = variablesAsStringMap;
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

  private Long id;

  private String firstName;

  private String lastName;

  private String firstNameSortable;

  private String lastNameSortable;

  private String authProvider;

  private String externalId;

  private String fullName;

  private fi.pyramus.domainmodel.users.Role role;

  private java.util.Set<fi.pyramus.domainmodel.base.Tag> tags;

  private java.util.Map<java.lang.String,java.lang.String> variablesAsStringMap;

  private Long version;

  private String title;

  public final static String[] properties = {"id","firstName","lastName","firstNameSortable","lastNameSortable","authProvider","externalId","fullName","role","tags","variablesAsStringMap","version","title"};
}
