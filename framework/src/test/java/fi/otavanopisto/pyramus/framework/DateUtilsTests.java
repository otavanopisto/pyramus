package fi.otavanopisto.pyramus.framework;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class DateUtilsTests {

  @Test
  public void testDateUtilsMax() {
    Calendar cal = Calendar.getInstance();

    cal.set(1990, 0, 1);
    Date first = cal.getTime();

    assertEquals(1990, 1900 + first.getYear());
    assertEquals(0, first.getMonth());
    assertEquals(1, first.getDate());

    cal.set(2001, 1, 2);
    Date second = cal.getTime();

    assertEquals(2001, 1900 + second.getYear());
    assertEquals(1, second.getMonth());
    assertEquals(2, second.getDate());
    
    Date max = DateUtils.max(first, second);
    
    assertEquals(2001, 1900 + max.getYear());
    assertEquals(1, max.getMonth());
    assertEquals(2, max.getDate());
  }
}
