package fi.otavanopisto.pyramus.framework;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

  /**
   * Returns true if the date part of the two given dates are different.
   * 
   * If both are null, there's no change -> returns false.
   * If one of the dates is null returns true.
   * Else compares the date portion of the Dates
   * 
   * @param d1 date 1
   * @param d2 date 2
   * @return true if the date part is different between the two dates
   */
  public static boolean dateChanged(Date d1, Date d2) {
    // Both null -> not changed (false)
    if (d1 == null && d2 == null) {
      return false;
    }

    // Other one is null, but because both weren't null, something changed (true)
    if (d1 == null || d2 == null) {
      return true;
    }
    
    // isSameDay wants non-null dates which they should be at this point
    return !org.apache.commons.lang3.time.DateUtils.isSameDay(d1, d2);
  }

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
  
  /**
   * Returns the date that is later of the two. Ignores nulls
   * such that if either is null the non-null will be returned.
   * If both are null, returns null.
   * 
   * @param date1
   * @param date2
   * @return
   */
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

  /**
   * Converts Date to LocalDate with current timezone.
   * 
   * If date is null, returns null.
   * 
   * @param date Date
   * @return LocalDate
   */
  public static LocalDate toLocalDate(Date date) {
    return date != null ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
  }

  /**
   * Converts LocalDate to Date with current timezone.
   * 
   * If localDate is null, returns null.
   * 
   * @param localDate
   * @return Date
   */
  public static Date toDate(LocalDate localDate) {
    return localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
  }
  
  /**
   * Converts Date to LocalDateTime with current timezone.
   * 
   * If date is null, returns null.
   * 
   * @param date Date
   * @return LocalDateTime
   */
  public static LocalDateTime toLocalDateTime(Date date) {
    return date != null ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
  }

  
}
