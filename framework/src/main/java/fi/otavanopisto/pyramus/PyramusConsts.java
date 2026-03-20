package fi.otavanopisto.pyramus;

import java.util.Set;

public class PyramusConsts {

  // Education type code for study programmes categorized under high school studies
  public static final String STUDYPROGRAMME_LUKIO = "lukio";
  
  // Study programme of paid course assessments
  public static final String STUDYPROGRAMME_PAID_ASSESSMENTS = "Aineopiskelu/lukio";
  
  // Curriculum names
  public static final String OPS_2016 = "OPS 2016";
  public static final String OPS_2018 = "OPS 2018";
  public static final String OPS_2021 = "OPS 2021";
  
  // Education type codes
  public static final String EDUCATION_TYPE_PK = "peruskoulu";
  public static final String EDUCATION_TYPE_LUKIO = "lukio";
  
  // Education subtype codes
  public static final String EDUCATION_SUBTYPE_MANDATORY = "pakollinen";
  public static final String EDUCATION_SUBTYPE_NATIONAL_OPTIONAL = "valtakunnallinensyventava";
  public static final String EDUCATION_SUBTYPE_SCHOOL_OPTIONAL = "koulukohtainensyventava";
  
  // Education time unit symbols
  public static final String TIMEUNIT_OP = "op";
  public static final String TIMEUNIT_HOURS = "h";
  
  // Course participation types
  public static final String PARTICIPATION_CANCELLED = "Keskeyttänyt";
  
  // Student's subject choices
  public static final String USERVARIABLE_SUBJECT_CHOICES_AIDINKIELI = "lukioAidinkieli";
  public static final String USERVARIABLE_SUBJECT_CHOICES_USKONTO = "lukioUskonto";
  public static final String USERVARIABLE_SUBJECT_CHOICES_MATEMATIIKKA = "lukioMatematiikka";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_A = "lukioKieliA";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B1 = "lukioKieliB1";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B2 = "lukioKieliB2";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B3 = "lukioKieliB3";
  
  // Subject choices
  public static final Set<String> CHOICE_SUBJECTS = Set.of("MAA", "MAB", "ÄI", "S2", "UE", "UO", "UI", "UK", "UJ", "UX", "ET");
  
  public static final String NOREPLY_EMAIL = "no-reply@muikkuverkko.fi";
  
  /**
   * SettingKeys
   */
  public static class Setting {
    // Comma separated list of GradingScale ids used for importing credits from a json file for a lukio student
    public static final String TRANSFERCREDITIMPORTS_GRADINGSCALES_LUKIO = "transfercredits.import.gradingscales.lukio";
  }
  
  public static class Matriculation {
    public static final String USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE = "matriculation.examEnrollmentExpiryDate";
    public static final String SETTING_ELIGIBLE_GROUPS = "matriculation.eligibleGroups";
  }
}
