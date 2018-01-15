package fi.internetix.smvc;

public class InvalidLoginException extends SmvcRuntimeException {

  private static final long serialVersionUID = -3587854318001354822L;
  
  public InvalidLoginException(String message) {
    super(StatusCode.UNAUTHORIZED, message);
  }
}
