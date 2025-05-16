package fi.otavanopisto.pyramus.students.tor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import fi.otavanopisto.pyramus.tor.TORCredit;
import fi.otavanopisto.pyramus.tor.TORCreditType;

public class TORCreditTests {

  private Date date(int year, int month, int day) {
    return Date.from(LocalDate.of(year, month, day).atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant());
  }
  
  @Test
  public void testGradeOrdering() {
    List<TORCredit> list = new ArrayList<>();
    
    list.add(new TORCredit(1l, "10", 10d, date(2010, 1, 1), TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "8", 8d, date(2010, 1, 1), TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "6", 6d, date(2010, 1, 1), TORCreditType.COURSEASSESSMENT, true));

    Collections.sort(list);
    
    assertEquals(Double.valueOf(6d), list.get(0).getNumericGrade());
    assertEquals(Double.valueOf(8d), list.get(1).getNumericGrade());
    assertEquals(Double.valueOf(10d), list.get(2).getNumericGrade());
  }

  @Test
  public void testPassingGradeOrdering() {
    List<TORCredit> list = new ArrayList<>();
    
    list.add(new TORCredit(1l, "8", 8d, date(2010, 1, 1), TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "S", null, date(2010, 1, 1), TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "H", null, date(2010, 1, 1), TORCreditType.COURSEASSESSMENT, false));
    list.add(new TORCredit(1l, "6", 6d, date(2010, 1, 1), TORCreditType.COURSEASSESSMENT, true));

    Collections.sort(list);
    
    assertTrue("H".equals(list.get(0).getGradeName()));
    assertTrue("S".equals(list.get(1).getGradeName()));
    assertTrue("6".equals(list.get(2).getGradeName()));
    assertTrue("8".equals(list.get(3).getGradeName()));
  }

  @Test
  public void testDateOrdering() {
    List<TORCredit> list = new ArrayList<>();

    Date first = date(2010, 1, 1);
    Date second = date(2010, 2, 2);
    Date third = date(2010, 3, 3);
    
    list.add(new TORCredit(1l, "8", 8d, second, TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "8", 8d, first, TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "8", 8d, third, TORCreditType.COURSEASSESSMENT, true));

    Collections.sort(list);
    
    assertTrue(first.equals(list.get(0).getDate()));
    assertTrue(second.equals(list.get(1).getDate()));
    assertTrue(third.equals(list.get(2).getDate()));
  }

  @Test
  public void testDateOrderingNullsFirst() {
    List<TORCredit> list = new ArrayList<>();

    Date first = null;
    Date second = date(2010, 2, 2);
    Date third = date(2010, 3, 3);
    
    list.add(new TORCredit(1l, "8", 8d, second, TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "8", 8d, first, TORCreditType.COURSEASSESSMENT, true));
    list.add(new TORCredit(1l, "8", 8d, third, TORCreditType.COURSEASSESSMENT, true));

    Collections.sort(list);
    
    assertTrue(Objects.equals(first, list.get(0).getDate()));
    assertTrue(Objects.equals(second, list.get(1).getDate()));
    assertTrue(Objects.equals(third, list.get(2).getDate()));
  }
  
}
