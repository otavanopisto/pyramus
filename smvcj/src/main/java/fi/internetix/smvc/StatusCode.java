package fi.internetix.smvc;

/** The status codes SMVCJ requests can have.
 *
 */
public class StatusCode {
  
  // Generic status codes  
  
  public static final int UNDEFINED = -1;
  public static final int OK = 0;
  
  public static final int NOT_LOGGED_IN = 100;
  public static final int UNAUTHORIZED = 101;
  public static final int PAGE_NOT_FOUND = 102;
  public static final int VALIDATION_FAILURE = 103;
  public static final int FILE_HANDLING_FAILURE = 104;

}