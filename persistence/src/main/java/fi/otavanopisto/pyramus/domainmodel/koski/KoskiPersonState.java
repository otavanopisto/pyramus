package fi.otavanopisto.pyramus.domainmodel.koski;

public enum KoskiPersonState {

  PENDING,
  SUCCESS,
  SERVER_FAILURE,
  SERVER_UNAVAILABLE,
  UNKNOWN_FAILURE, 
  NO_CURRICULUM,
  NO_UNIQUE_ID,
  NO_RESOLVABLE_SUBJECTS,
  LINKED_MISSING_VALUES,
  UNKNOWN_LANGUAGE,
  UNRESOLVED_CREDIT_CURRICULUM,
  UNREPORTED_CREDIT,
  UNRESOLVABLE_SUBTYPES,
  MISSING_STUDYAPPROVER;
  
  /* Pending states */
  public static KoskiPersonState[] PENDING_STATES = { PENDING };
  /* Success states */
  public static KoskiPersonState[] SUCCESS_STATES = { SUCCESS };
  /* Warning states which indicate succeeded update but with warnings */
  public static KoskiPersonState[] WARNING_STATES = { LINKED_MISSING_VALUES, NO_RESOLVABLE_SUBJECTS, UNKNOWN_LANGUAGE, 
      UNRESOLVED_CREDIT_CURRICULUM, UNREPORTED_CREDIT, UNRESOLVABLE_SUBTYPES };
  /* Error states which prevent the update altogether */
  public static KoskiPersonState[] ERROR_STATES = { SERVER_FAILURE, SERVER_UNAVAILABLE, UNKNOWN_FAILURE, NO_CURRICULUM, 
      NO_UNIQUE_ID, MISSING_STUDYAPPROVER};
}
