package fi.otavanopisto.pyramus.rest.model.muikku;

public class CredentialResetPayload {

  public CredentialResetPayload() {
    super();
  }

  public CredentialResetPayload(String secret, String username, String password) {
    this.secret = secret;
    this.username = username;
    this.password = password;
  }

  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  private String secret;
  private String username;
  private String password;

}