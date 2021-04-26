package fi.otavanopisto.pyramus.koski;

public class KoskiConsts {

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
