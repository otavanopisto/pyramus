package fi.otavanopisto.pyramus.schedulers;

import java.util.Calendar;
import java.util.Date;

public class PastStudentDataThreshold {

  /**
   * Shortest threshold
   */
  public static Date primaryThreshold() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -12);
    return cal.getTime();
  }

  /**
   * Secondary, medium threshold
   */
  public static Date secondaryThreshold() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -5);
    return cal.getTime();
  }
  
  /**
   * Tertiary, longest threshold
   */
  public static Date tertiaryThreshold() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -10);
    return cal.getTime();
  }
  
}
