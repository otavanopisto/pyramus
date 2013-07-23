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

  private Long id;

  private String firstName;

  private String lastName;

  private String authProvider;

  private String externalId;

  private fi.pyramus.domainmodel.users.Role role;

  private Long version;

  private String title;

  public final static String[] properties = {"id","firstName","lastName","authProvider","externalId","role","version","title"};
}
