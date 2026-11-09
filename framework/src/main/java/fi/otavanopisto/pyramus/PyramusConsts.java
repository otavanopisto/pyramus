package fi.otavanopisto.pyramus;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.util.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.Version;

public class PyramusConsts {

  public static final Version FREEMARKER_COMPATIBILITY_VERSION = Configuration.VERSION_2_3_35;
  
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
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_A1 = "lukioKieliA1";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_A2 = "lukioKieliA2";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B1 = "lukioKieliB1";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B2 = "lukioKieliB2";
  public static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B3 = "lukioKieliB3";
  
  // Collection of all the subject choice variables
  public static final Set<String> USERVARIABLE_SUBJECT_CHOICE_VARIABLES = Set.of(
    USERVARIABLE_SUBJECT_CHOICES_AIDINKIELI,
    USERVARIABLE_SUBJECT_CHOICES_USKONTO,
    USERVARIABLE_SUBJECT_CHOICES_MATEMATIIKKA,
    USERVARIABLE_SUBJECT_CHOICES_KIELI_A,
    USERVARIABLE_SUBJECT_CHOICES_KIELI_A1,
    USERVARIABLE_SUBJECT_CHOICES_KIELI_A2,
    USERVARIABLE_SUBJECT_CHOICES_KIELI_B1,
    USERVARIABLE_SUBJECT_CHOICES_KIELI_B2,
    USERVARIABLE_SUBJECT_CHOICES_KIELI_B3
  );
  
  // Participation types
  public static final String PARTICIPATION_TYPE_PASS = "Suorittanut";
  public static final String PARTICIPATION_TYPE_FAIL = "Hylätty";
  public static final String PARTICIPATION_TYPE_ENROLLED = "Ilmoittautunut";
  
  /**
   * Lukio
   */
  public static class Lukio {
    public static final String EDUCATION_TYPE = EDUCATION_TYPE_LUKIO;

    // Subject choices
    public static final Set<String> CHOICE_SUBJECTS = Set.of("MAA", "MAB", "ÄI", "S2", "UE", "UO", "UI", "UK", "UJ", "UX", "ET");

    // Lukion uskonnon aineet. Elämänkatsomustieto (et) ei kuulu näihin, sille on oma lokeronsa.
    public static final Set<String> USKONTO       = Set.of("UE", "UO", "UI", "UK", "UJ", "UX");
    
    // Lukion uskonnon ainevalinnat. Elämänkatsomustieto (et) kuuluu näihin ja nämä on keskenään poissulkevia.
    public static final Set<String> USKONTO_JA_ET = Set.of("UE", "UO", "UI", "UK", "UJ", "UX", "ET");
    
    /**
     * Default subject selections for lukio. Map where the key is the 
     * user variable name and value is the default subject code.
     */
    public static final Map<String, String> DEFAULT_SUBJECT_CHOICES = Map.of(
        USERVARIABLE_SUBJECT_CHOICES_MATEMATIIKKA,     "MAB",
        USERVARIABLE_SUBJECT_CHOICES_AIDINKIELI,       "ÄI",
        USERVARIABLE_SUBJECT_CHOICES_USKONTO,          "UE"
    );
    
    /**
     * Returns true if the new religion reporting instructions which came in effect on 1.9.2026 apply to the given student.
     * 
     * @param student
     * @return
     */
    public static boolean uskonnonkirjaustapa2026(Student student) {
      LocalDate threshold = LocalDate.of(2026, 9, 1); // 1.9.2026
      LocalDate studyEndDate = DateUtils.toLocalDate(student.getStudyEndDate());
      
      return studyEndDate == null || studyEndDate.isEqual(threshold) || studyEndDate.isAfter(threshold);
    }
  }
  
  /**
   * Perusopetus
   */
  public static class Perusopetus {
    public static final String EDUCATION_TYPE = EDUCATION_TYPE_PK;
    
    // Subject choices
    public static final Set<String> CHOICE_SUBJECTS = Set.of("äi", "s2", "ue", "uo", "ui", "et");

    // Grades allowed by the curriculum
    public static final Set<String> ALLOWED_GRADES = Set.of("4", "5", "6", "7", "8", "9", "10", "S", "H");
    
    // Grades allowed by the curriculum
    public static final Set<String> PASSING_GRADES = Set.of("5", "6", "7", "8", "9", "10", "S");
    
    // Perusopetuksen uskonnon aineet. Elämänkatsomustieto (et) ei kuulu näihin, sille on oma lokeronsa.
    public static final Set<String> USKONTO       = Set.of("ue", "uo", "ui");
    
    // Perusopetuksen uskonnon ainevalinnat. Elämänkatsomustieto (et) kuuluu näihin ja nämä on keskenään poissulkevia.
    public static final Set<String> USKONTO_JA_ET = Set.of("ue", "uo", "ui", "et");
    
    /**
     * Default subject selections for perusopetus. Map where the key is the 
     * user variable name and value is the default subject code.
     */
    public static final Map<String, String> DEFAULT_SUBJECT_CHOICES = Map.of(
        USERVARIABLE_SUBJECT_CHOICES_AIDINKIELI,       "äi",
        USERVARIABLE_SUBJECT_CHOICES_USKONTO,          "ue"
    );
  }
  
  /**
   * Apa
   */
  public static class Apa {
    public static final String EDUCATION_TYPE = "apa";
    
    /**
     * Default subject selections for apa. Map where the key is the 
     * user variable name and value is the default subject code.
     */
    public static final Map<String, String> DEFAULT_SUBJECT_CHOICES = Map.of(
        // Not sure why this has historically always been A1 instead of just A.. Comment here if you figure it out
        USERVARIABLE_SUBJECT_CHOICES_KIELI_A1,         "aena"
    );
  }
  
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
