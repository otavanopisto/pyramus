package fi.pyramus.rest.model;

public class UserCredentials {

  public UserCredentials() {
    super();
  }

  public UserCredentials(String oldPassword, String username, String newPassword) {
    this.oldPassword = oldPassword;
    this.username = username;
    this.newPassword = newPassword;
  }

  public String getOldPassword() {
    return oldPassword;
  }
  
  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }
  
  public String getNewPassword() {
    return newPassword;
  }
  
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
  
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  private String oldPassword;
  private String newPassword;
  private String username;
}
