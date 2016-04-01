package fi.otavanopisto.pyramus.framework;

public class UserEmailInUseException extends Exception {

  private static final long serialVersionUID = 7092208351027170769L;

  public UserEmailInUseException() {
    super("Email address is in use.");
  }
}
