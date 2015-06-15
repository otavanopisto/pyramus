package fi.pyramus.rest.model;

public class UserCredentialReset {

  public UserCredentialReset() {
    super();
  }

  public UserCredentialReset(String secret, String newPassword) {
    this.secret = secret;
    this.newPassword = newPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }
  
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
  
  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  private String secret;
  private String newPassword;
}
