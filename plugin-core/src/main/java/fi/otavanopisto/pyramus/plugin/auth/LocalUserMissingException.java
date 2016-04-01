package fi.otavanopisto.pyramus.plugin.auth;

public class LocalUserMissingException extends AuthenticationException {

  private static final long serialVersionUID = -7235126725998529622L;

  public LocalUserMissingException(String externalUser) {
    super(LOCAL_USER_MISSING);
    this.externalUser = externalUser;
  }
  
  public String getExternalUser() {
    return externalUser;
  }
  
  private String externalUser;
}
