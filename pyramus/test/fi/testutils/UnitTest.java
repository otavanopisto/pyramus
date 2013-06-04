package fi.testutils;

import java.util.Calendar;
import java.util.Date;

public class UnitTest {

  protected static Date createDate(int year, int month, int date) {
    Calendar c = Calendar.getInstance();
    c.set(year, month, date);
    return c.getTime();
  }
  
}
