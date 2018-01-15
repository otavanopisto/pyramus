package fi.internetix.smvc;

public class AlreadyLoggedInException extends SmvcRuntimeException {

  private static final long serialVersionUID = -5408580026160001631L;

  public AlreadyLoggedInException(int statusCode, String message) {
    super(statusCode, message);
  }
}
