package fi.otavanopisto.pyramus.framework;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

  public static Date setTime(Date date, int hours, int minutes, int seconds) {
    if (date == null) {
      return null;
    }
    
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR, hours);
    c.set(Calendar.MINUTE, minutes);
    c.set(Calendar.SECOND, seconds);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTime();
  }
  
  public static Date startOfDay(Date date) {
    return setTime(date, 0, 0, 0);
  }
  
  public static Date endOfDay(Date date) {
    return setTime(date, 23, 59, 59);
  }
  
  public static Date max(Date date1, Date date2) {
    if (date1 == null && date2 == null) {
      return null;
    }
    
    if (date1 == null) {
      return date2;
    } else if (date2 == null) {
      return date1;
    }
    
    return date1.after(date2) ? date1 : date2;
  }

  /**
   * Returns true if date (first parameter) is after earlierCandidate (second parameter)
   * or if second parameter is null. Throws NPE if date is null.
   */
  public static boolean isAfterOrNull(Date date, Date earlierCandidate) {
    return earlierCandidate != null ? date.after(earlierCandidate) : true;
  }
  
  /**
   * Returns true if date is within the timeframe specified by start and end dates. The start
   * or end dates can be null, in which case they are not compared against. I.e. if only
   * start date is specified, the date is only checked to be after the start date.
   * 
   * @param date the date
   * @param start start date of the timeframe
   * @param end end date of the timeframe
   * @return true, if the date is between the start and end dates or exactly equal to either one
   */
  public static boolean isWithin(Date date, Date start, Date end) {
    if (date == null) {
      throw new IllegalArgumentException();
    }
    
    if (start != null && date.compareTo(start) < 0) {
      return false;
    }
    
    if (end != null && date.compareTo(end) > 0) {
      return false;
    }
    
    return true;
  }
}
