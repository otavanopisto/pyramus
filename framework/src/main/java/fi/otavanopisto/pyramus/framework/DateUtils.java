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
  
}
