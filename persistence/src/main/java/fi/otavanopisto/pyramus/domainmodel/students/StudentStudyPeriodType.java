package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.EnumSet;

public enum StudentStudyPeriodType {

  TEMPORARILY_SUSPENDED,
  PROLONGED_STUDYENDDATE,
  COMPULSORY_EDUCATION,
  NON_COMPULSORY_EDUCATION,
  EXTENDED_COMPULSORY_EDUCATION;

  /**
   * Periods that only have a start date
   */
  public static final EnumSet<StudentStudyPeriodType> BEGINDATE_ONLY = EnumSet.of(
      PROLONGED_STUDYENDDATE,
      NON_COMPULSORY_EDUCATION
  );
  
  /**
   * Period end date can be beyond Student's study end date
   */
  public static final EnumSet<StudentStudyPeriodType> ALLOW_PERIOD_END_OUTSIDE_STUDYTIME = EnumSet.of(
      COMPULSORY_EDUCATION, 
      EXTENDED_COMPULSORY_EDUCATION
  );
  
}
