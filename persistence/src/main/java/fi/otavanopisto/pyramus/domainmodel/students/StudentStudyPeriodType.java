package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.EnumSet;

public enum StudentStudyPeriodType {

  TEMPORARILY_SUSPENDED,
  PROLONGED_STUDYENDDATE,
  COMPULSORY_EDUCATION,
  NON_COMPULSORY_EDUCATION,
  EXTENDED_COMPULSORY_EDUCATION;

  public static final EnumSet<StudentStudyPeriodType> BEGINDATE_ONLY = EnumSet.of(
      PROLONGED_STUDYENDDATE,
      COMPULSORY_EDUCATION,
      NON_COMPULSORY_EDUCATION
  );
  
}
