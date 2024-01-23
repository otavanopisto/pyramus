package fi.otavanopisto.pyramus.koski;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class KoskiConsts {

  public static final List<String> LUKIO2019_DIAARIT = Arrays.asList(new String[] { "OPH-2263-2019", "OPH-2267-2019" });

  public static class Setting {
    public static final String KOSKI_SETTINGKEY_BASEURL = "koski.baseUrl";
    public static final String KOSKI_SETTINGKEY_AUTH = "koski.auth";
    public static final String KOSKI_SETTINGKEY_CSRF = "koski.csrf";
  }
  
  public static class VariableNames {
    // Persons' oid
    public static final String KOSKI_HENKILO_OID = "koski.henkilo-oid";
    // Study permission oids
    public static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
    public static final String KOSKI_INTERNETIX_STUDYPERMISSION_ID = "koski.internetix-studypermission-id";
    // Linked study permission oid
    public static final String KOSKI_LINKED_STUDYPERMISSION_ID = "koski.linked-to-studypermission-id";
    // Skipped from integration
    public static final String KOSKI_SKIPPED_STUDENT = "koski.skippedStudent";
  }

  public static class UserVariables {
    public static final String STARTED_UNDER18 = "lukioAlle18v";
    public static final String UNDER18_STARTREASON = "under18studyStartReason";
    public static final String PK_GRADE_UPGRADE = "perusopetusKorottaja";
  }

  public static class SchoolVariables {
    public static final String KOSKI_SCHOOL_OID = "koski.schooloid";
  }
  
  public static class SubjectSelections {
    public static final String MATH = "lukioMatematiikka";
    public static final String NATIVE_LANGUAGE = "lukioAidinkieli";
    public static final String LANG_A = "lukioKieliA";
    public static final String LANG_A1 = "lukioKieliA1";
    public static final String LANG_A2 = "lukioKieliA2";
    public static final String LANG_B1 = "lukioKieliB1";
    public static final String LANG_B2 = "lukioKieliB2";
    public static final String LANG_B3 = "lukioKieliB3";
    public static final String RELIGION = "lukioUskonto";
    public static final String COMPLETION_MARKS = "lukioSmerkinta";
    
    /**
     * Set of variable names that may contain multiple subject selections 
     * (variable value is comma separated list of subject codes).
     */
    public static final Set<String> MULTIPLE_SELECTIONS = Set.of(
        LANG_A, LANG_A1, LANG_A2, LANG_B1, LANG_B2, LANG_B3
    );
    
    public static final Map<String, Set<String>> VARIABLE_SUBJECTCODES = Map.of(
        MATH,                   Set.of("MAA", "MAB"),
        NATIVE_LANGUAGE,        Set.of("ÄI", "S2"),
        LANG_A,                 Set.of("ENA", "RAA", "RUA", "SAA", "VEA"),
        LANG_A1,                Set.of(),
        LANG_A2,                Set.of(),
        LANG_B1,                Set.of("RUB", "RUB1"),
        LANG_B2,                Set.of("EAB2", "IAB2", "RAB2", "SAB2", "VEB2"),
        LANG_B3,                Set.of("EAB3", "IAB3", "RAB3", "SAB3", "VEB3", 
                                       "RUB3", "SMB3", "LAB3", "ARB3", "JPB3", "KOB3", "KXB3"),
        RELIGION,               Set.of("UE", "UO", "UI", "UK", "UJ", "UX", "ET")
    );
    
    /**
     * Returns the variable name where given subject code should be stored in.
     * 
     * @param subjectCode
     * @return
     */
    public static final String getVariableFromSubjectCode(String subjectCode) {
      if (!StringUtils.isBlank(subjectCode)) {
        for (Entry<String, Set<String>> entry : VARIABLE_SUBJECTCODES.entrySet()) {
          if (entry.getValue().contains(subjectCode)) {
            return entry.getKey();
          }
        }
      }
      
      return null;
    }
  }

  public static String getStudentIdentifier(KoskiStudyProgrammeHandler handler, Long studentId) {
    return handler.name() + ":" + String.valueOf(studentId);
  }

}
