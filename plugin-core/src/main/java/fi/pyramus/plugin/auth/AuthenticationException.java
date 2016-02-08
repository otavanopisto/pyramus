package fi.pyramus.plugin.auth;

/**
 * Exception class for errors during authentication.
 */
public class AuthenticationException extends Exception {

  /** Constant for unauthorized access */
  public static final int UNAUTHORIZED = 1;
  /** Constant for local user missing */
  protected static final int LOCAL_USER_MISSING = 2;
  /** Constant for email belonging to another person */
  public static final int EMAIL_BELONGS_TO_ANOTHER_PERSON = 3;
  /** Constant for problem caused by external authentication server */
  public static final int EXTERNAL_LOGIN_SERVER_ERROR = 4;
  

  /**
   * Constructor specifying the error code of this exception. The code should be one of the
   * constants defined in this class.
   * 
   * @param errorCode The error code
   */
  public AuthenticationException(int errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * Returns the error code of this exception.
   * 
   * @return The error code of this exception
   */
  public int getErrorCode() {
    return errorCode;
  }

  /** The error code of this exception */
  private int errorCode;

  /** The serial version UID of this exception */
  private static final long serialVersionUID = -2587092421939362393L;

}
