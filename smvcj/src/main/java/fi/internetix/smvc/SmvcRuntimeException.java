package fi.internetix.smvc;

/**
 * The base exception class for all gracefully handled error situations in the application.
 */
public class SmvcRuntimeException extends RuntimeException {

  /** The serial version UID of the class */
  private static final long serialVersionUID = -5069996150452823136L;

  public SmvcRuntimeException(Exception e) {
    super(e);
  }

  public SmvcRuntimeException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public SmvcRuntimeException(int statusCode, String message, Exception e) {
    super(message, e);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }

  private int statusCode = StatusCode.UNDEFINED;
}
