package fi.otavanopisto.pyramus.services.entities.users;

public class UserEntity {
  
  public UserEntity() {
  }

  public UserEntity(Long id, String[] emails, String firstName, String lastName, String[] tags) { //, String externalId, String authProvider, String role) {
    this.id = id;
    this.emails = emails;
    this.firstName = firstName;
    this.lastName = lastName;
    this.tags = tags;
//    this.externalId = externalId;
//    this.authProvider = authProvider;
//    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String[] getEmails() {
    return emails;
  }

  public void setEmails(String[] emails) {
    this.emails = emails;
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

//  public String getExternalId() {
//    return externalId;
//  }
//
//  public void setExternalId(String externalId) {
//    this.externalId = externalId;
//  }
//
//  public String getAuthProvider() {
//    return authProvider;
//  }
//
//  public void setAuthProvider(String authProvider) {
//    this.authProvider = authProvider;
//  }
//
//  public String getRole() {
//    return role;
//  }
//
//  public void setRole(String role) {
//    this.role = role;
//  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  private Long id;
  private String[] emails;
  private String firstName;
  private String lastName;
//  private String externalId;
//  private String authProvider;
//  private String role;
  private String[] tags;
}
