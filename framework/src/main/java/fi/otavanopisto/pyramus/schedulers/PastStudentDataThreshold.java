package fi.otavanopisto.pyramus.schedulers;

import java.util.Calendar;
import java.util.Date;

import fi.otavanopisto.pyramus.domainmodel.students.Student;

public class PastStudentDataThreshold {

  /**
   * Returns true if given date matches (is before) any of the thresholds.
   * 
   * @param date date
   * @return true if given date matches (is before) any of the thresholds
   */
  public static boolean matchesAnyThreshold(Date date) {
    return
        PastStudentDataThreshold.primaryThreshold().after(date)
        || PastStudentDataThreshold.secondaryThreshold().after(date)
        || PastStudentDataThreshold.tertiaryThreshold().after(date);
  }

  /**
   * Returns true if student matches given threshold.
   * 
   * @param student student
   * @param treshold threshold
   * @return true if student matches given threshold
   */
  public static boolean matchesThreshold(Student student, Date treshold) {
    if (student.getStudyEndDate() == null || student.getStudyEndDate().after(treshold)) {
      return false;
    }
    
    return true;
  }
  
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
