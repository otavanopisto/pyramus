package fi.otavanopisto.pyramus.koski;

import java.util.Arrays;
import java.util.List;

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
    // Study permission oid
    public static final String KOSKI_STUDYPERMISSION_ID = "koski.studypermission-id";
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
  }

  public static String getStudentIdentifier(KoskiStudyProgrammeHandler handler, Long studentId) {
    return handler.name() + ":" + String.valueOf(studentId);
  }

}
