package fi.pyramus.framework;

import fi.internetix.smvc.StatusCode;

public class PyramusStatusCode extends StatusCode {

  public static final int ALREADY_LOGGED_IN = 1000;
  public static final int LOCAL_USER_MISSING = 1001;
  public static final int PASSWORD_MISMATCH = 1002;
  public static final int CONCURRENT_MODIFICATION = 1003;
  
}
