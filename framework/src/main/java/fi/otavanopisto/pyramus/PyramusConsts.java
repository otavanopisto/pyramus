package fi.otavanopisto.pyramus;

public class PyramusConsts {

  // Education type code for study programmes categorized under high school studies
  public static final String STUDYPROGRAMME_LUKIO = "lukio";
  
  // Study programme of paid course assessments
  public static final String STUDYPROGRAMME_PAID_ASSESSMENTS = "Aineopiskelu/lukio";
  
  // Curriculum names
  public static final String OPS_2016 = "OPS 2016";
  public static final String OPS_2018 = "OPS 2018";
  public static final String OPS_2021 = "OPS 2021";
  
  // Subject education types
  public static final String SUBJECT_PERUSOPETUS = "Perusopetus";
  public static final String SUBJECT_LUKIO = "Lukio";
  
  // Education time unit symbols
  public static final String TIMEUNIT_OP = "op";
  public static final String TIMEUNIT_HOURS = "h";
  
  /**
   * SettingKeys
   */
  public static class Setting {
    // Comma separated list of GradingScale ids used for importing credits from a json file for a lukio student
    public static final String TRANSFERCREDITIMPORTS_GRADINGSCALES_LUKIO = "transfercredits.import.gradingscales.lukio";
  }
  
}
